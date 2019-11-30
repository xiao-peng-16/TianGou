package com.cxp.shop_commodity.service.impl;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.request.SearchRequest;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.*;
import com.cxp.shop_commodity.mapper.CommodityMapper;
import com.cxp.shop_commodity.service.CommodityService;
import com.cxp.shop_commodity.service.FeignClient.OrderFeignClient;
import com.cxp.shop_commodity.service.FeignClient.StoreFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    CommodityMapper commodityMapper;

    @Autowired
    OrderFeignClient orderFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;


    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean COMMODITY_ID_ERROR = ResultFactory.createFailResult(ResultStatus.COMMODITY_ID_ERROR);
    static final ResultBean COMMODITY_STOCK_INSUFFICIENT = ResultFactory.createFailResult(ResultStatus.COMMODITY_STOCK_INSUFFICIENT);
    static final ResultBean STORE_EQUAL_USER_ERROR = ResultFactory.createFailResult(ResultStatus.STORE_EQUAL_USER_ERROR);

    @Override
    public Map<Integer, OrderCommodityVO> mapOrderCommodityVO(List<Integer> commodityIdList) {
        return commodityMapper.mapOrderCommodityVO(commodityIdList);
    }

    @Override
    public Map<Integer, FavoriteCommodityVO> mapFavoriteCommodityVO(List<Integer> commodityIdList) {
        return commodityMapper.mapFavoriteCommodityVO(commodityIdList);
    }

    @Override
    public Commodity selCommodityByCommodityID(int commodityId) {
        //更新商品人气
        commodityMapper.updAddCPopularityByID(commodityId);
        Commodity commodity = commodityMapper.selCommodityByCommodityID(commodityId);
        commodity.setStore( storeFeignClient.selStoreByStoreId(commodity.getStoreId()));
        return commodity;
    }


    //********************************    搜索页  -开始**************************************
    //设置分页
    public void setPageStartLen(SearchRequest searchPage_request) {
        if(searchPage_request.getPageNo()!=null && searchPage_request.getPageStepSize()!=null){
            searchPage_request.setPageStartLen((searchPage_request.getPageNo()-1) * searchPage_request.getPageStepSize());
        }else {
            if (searchPage_request.getPageNo() ==null)
                searchPage_request.setPageStartLen(0);  //第0条开始
            if (searchPage_request.getPageStepSize() == null)
                searchPage_request.setPageStepSize(20); //每次最多20条
        }
    }

    //给根据商品名字 和 种类名字  查询商品到的商品信息 添加店铺信息
    private void addStore(List<SearchCommodityVO> commoditySearchVOList){
        List<Integer> storeIdList = commoditySearchVOList.stream().map(e -> e.getStoreId()).collect(Collectors.toList());
        Map<Integer, StoreToCommodity> storeId_storeToCommodityMap = storeFeignClient.mapStoreToCommodityByStoreId(storeIdList);
        for (SearchCommodityVO commoditySearchVO : commoditySearchVOList)
            commoditySearchVO.setStoreToCommodity( storeId_storeToCommodityMap.get(commoditySearchVO.getStoreId()) );
    }
    //根据商品名字查询
    private SearchVO searchByCommodityName(SearchRequest searchPage_request) {
        Integer commodityCount = commodityMapper.selSearchCountByCommodityName(searchPage_request);
        if (0 == commodityCount)
            return null;
        List<SearchCommodityVO> commoditySearchVOList = commodityMapper.selSearchCommodityVOByCommodityName(searchPage_request);
        addStore(commoditySearchVOList);
        SearchVO searchPage = new SearchVO(commodityCount, commoditySearchVOList);
        return searchPage;
    }
    //根据种类的名字查询
    private SearchVO searchBySortName(SearchRequest searchPage_request) {
        Integer commodityCount = commodityMapper.selSearchCountBySortName(searchPage_request);
        if (0 == commodityCount)
            return null;
        List<SearchCommodityVO> commoditySearchVOList = commodityMapper.selSearchCommodityVOBySortName(searchPage_request);
        addStore(commoditySearchVOList);
        SearchVO searchPage = new SearchVO(commodityCount, commoditySearchVOList);
        return searchPage;
    }

    //根据店铺名字查询
    private SearchVO searchByStoreName(SearchRequest searchPage_request) {

        Map<Integer, StoreToCommodity> storeId_storeToCommodityMap = storeFeignClient.selStoreToCommodityMapByStoreName(searchPage_request.getSearchWord());
        if (0 == storeId_storeToCommodityMap.size())
            return null;
        List<Integer> storeIdList = storeId_storeToCommodityMap.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());

        int commodityCount = commodityMapper.selSearchCountByStoreId(storeIdList);
        if (0 == commodityCount)
            return null;

        List<SearchCommodityVO> commoditySearchVOList = commodityMapper.selSearchCommodityVOByStoreId(storeIdList, searchPage_request);
        for (SearchCommodityVO commoditySearchVO : commoditySearchVOList)
            commoditySearchVO.setStoreToCommodity( storeId_storeToCommodityMap.get(commoditySearchVO.getStoreId()) );

        SearchVO searchPage = new SearchVO(commodityCount, commoditySearchVOList);
        return searchPage;
    }

    @Override
    public SearchVO selSearchVO(SearchRequest searchPage_request) {
        setPageStartLen(searchPage_request);
        SearchVO searchPage = null;
        if (searchPage_request.getSearchWord().contains("店")){
            //查询顺序 店铺名字 -> 种类名字 -> 商品名字
            searchPage = searchByStoreName(searchPage_request);
            if (null == searchPage)
                searchPage = searchBySortName(searchPage_request);
            if (null == searchPage)
                searchPage = searchByCommodityName(searchPage_request);
        }else {
            //查询顺序 种类名字 -> 商品名字 -> 店铺名字
            searchPage = searchBySortName(searchPage_request);
            if (null == searchPage)
                searchPage = searchByCommodityName(searchPage_request);
            if (null == searchPage)
                searchPage = searchByStoreName(searchPage_request);
        }
        return searchPage;
    }


    //********************************    搜索页  -结束**************************************




    @Override
    public Map<Integer, ShopCarCommodityVO> mapShopCarCommodityVO(List<Integer> commodityIdList) {
        return commodityMapper.mapShopCarCommodityVO(commodityIdList);
    }


    @Override
    public boolean isCommodityStoreEqualUser(int userId, int commodityId) {
        return 0 != commodityMapper.selcommodityStoreEqualUser(userId, commodityId);
    }


    private boolean setOrderSonReturnIsFail(OrderSon orderSon, CommodityToOrder commodityToOrder){
        if (commodityToOrder.getCommodityStock() < orderSon.getChooseNumber())
            return true;
        orderSon.setStoreId(commodityToOrder.getStoreId())
                .setCommodityPrice(commodityToOrder.getCommodityPrice())
                .setOrderSumPrice(commodityToOrder.getCommodityPrice() * orderSon.getChooseNumber());
        return false;
    }
    @Override
    public ResultBean submitOrder(Integer userId, LinkedList<OrderSon> orderSonList){

        Map<Integer, CommodityToOrder> commodityIdToOrderMap = commodityMapper.selCommodityToOrderMap(orderSonList);

        //找不到任何商品信息 商品信息一个都找不到
        if (0 ==commodityIdToOrderMap.size())   //购物车中
            return COMMODITY_ID_ERROR;

        //直接从商品页购买 或购物车只有一件商品
        if (1 == orderSonList.size()){
            OrderSon orderSon = orderSonList.get(0);
            CommodityToOrder commodityToOrder = commodityIdToOrderMap.get(orderSon.getCommodityId());
            if (0 ==commodityIdToOrderMap.size())
                return COMMODITY_ID_ERROR;
            if (userId == commodityToOrder.getStoreId())
                return STORE_EQUAL_USER_ERROR;
            if (setOrderSonReturnIsFail(orderSon, commodityToOrder))
                return COMMODITY_STOCK_INSUFFICIENT;
        }else { //加入购物车的  都不存在用户购买自己商品的问题，有问题加入不了购物车
            for ( OrderSon orderSon : orderSonList) {
                CommodityToOrder commodityToOrder = commodityIdToOrderMap.get(orderSon.getCommodityId());
                if (commodityToOrder == null)
                    orderSonList.remove(orderSon);  //商品信息都找不到 剔除该条
                else if (setOrderSonReturnIsFail(orderSon, commodityToOrder))
                    orderSonList.remove(orderSon);  //商品库存不足 剔除该条
            }
            if (0 ==orderSonList.size())          //购物车中 剩下 商品库存都不足
                return COMMODITY_STOCK_INSUFFICIENT;
        }
        OrderParent orderParent = new OrderParent(userId);
        orderParent.setOrderSonList(orderSonList);
        //返回 订单微服务 响应结果
        return orderFeignClient.addOrder(orderParent) ;
    }

    @Override
    public ResultBean updCommodityNumber(List<CommodityNumberChange> commodityNumberChangeList) {
        for (CommodityNumberChange commodityNumberChange :commodityNumberChangeList){
            commodityMapper.updCommodityNumber(commodityNumberChange);
        }
        return successResult;
    }

    @Override
    public List<StoreCommodityVO> selStoreCommodityByUserId(int userId) {
        return commodityMapper.selStoreCommodityByUserId(userId);
    }

}
