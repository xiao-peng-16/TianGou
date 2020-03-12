package com.cxp.shop_commodity.service.impl;

import com.cxp.shop_api.dto.CommodityToCart;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.Sort;
import com.cxp.shop_api.entity.StoreBase;
import com.cxp.shop_api.request.SearchRequest;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.*;
import com.cxp.shop_commodity.mapper.CommodityMapper;
import com.cxp.shop_commodity.pojo.CommodityPhotoVideo;
import com.cxp.shop_commodity.service.CommodityService;
import com.cxp.shop_commodity.service.FeignClient.ImagesFeignClient;
import com.cxp.shop_commodity.service.FeignClient.StoreFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    CommodityMapper commodityMapper;

    @Autowired
    StoreFeignClient storeFeignClient;

    @Autowired
    ImagesFeignClient imagesFeignClient;


    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean COMMODITY_INSERT_ERROR = ResultFactory.createFailResult(ResultStatus.COMMODITY_INSERT_ERROR);
    static final ResultBean COMMODITY_UPDATE_ERROR = ResultFactory.createFailResult(ResultStatus.COMMODITY_UPDATE_ERROR);
    static final ResultBean COMMODITY_STOCK_INSUFFICIENT = ResultFactory.createFailResult(ResultStatus.COMMODITY_STOCK_INSUFFICIENT);
    static final ResultBean COMMODITY_NOT_FOUND = ResultFactory.createFailResult(ResultStatus.COMMODITY_NOT_FOUND);
    static final ResultBean STORE_EQUAL_USER_ERROR = ResultFactory.createFailResult(ResultStatus.STORE_EQUAL_USER_ERROR);



    @Override
    public List<Sort> listSort() {
        return commodityMapper.listSort();
    }

    @Override
    public ResultBean addCommodity(Commodity commodity) {
        return 0 < commodityMapper.insCommodity(commodity) ? successResult : COMMODITY_INSERT_ERROR;
    }

    @Override
    public ResultBean updCommodity(Commodity commodity) {

        CommodityPhotoVideo commodityPhotoVideo = commodityMapper.getCommodityPhotoVideo(commodity.getCommodityId());

        if (0 == commodityMapper.updCommodity(commodity))
            return COMMODITY_UPDATE_ERROR;

        ArrayList<String> urlList = new ArrayList<>();

        String oldCommodityVideo = commodityPhotoVideo.getCommodityVideo();
        String newCommodityVideo = commodity.getCommodityVideo();

        if (null != oldCommodityVideo && !oldCommodityVideo.equals(newCommodityVideo))
            urlList.add(oldCommodityVideo);

        if (0 != urlList.size())
            imagesFeignClient.dlelQiniuImagesList(urlList);

        return successResult;
    }

    @Override
    public boolean updCommodityOnShelves(Integer storeId, Integer commodityId, Boolean commodityOnShelves) {
        if (null == commodityId || null == commodityOnShelves)
            return false;
        return 0 != commodityMapper.updCommodityOnShelves(storeId, commodityId, commodityOnShelves);
    }

    @Override
    public boolean delCommodity(Integer storeId, Integer commodityId) {
        return 0 != commodityMapper.delCommodity(storeId, commodityId);
    }


    @Override
    public Map<Integer, FavoriteCommodityVO> mapFavoriteCommodityVO(List<Integer> commodityIdList) {
        return commodityMapper.mapFavoriteCommodityVO(commodityIdList);
    }

    @Override
    public Commodity getCommodityPageByCommodityId(int commodityId) {
        //更新商品人气
        commodityMapper.updAddCPopularityByID(commodityId);
        Commodity commodity = commodityMapper.getCommodityByCommodityId(commodityId);
        if (null == commodity)
            return null;
        commodity.setStore( storeFeignClient.getStoreByStoreId(commodity.getStoreId()));
        return commodity;
    }

    @Override
    public Commodity getCommodityByCommodityId(int commodityId) {
        return commodityMapper.getCommodityByCommodityId(commodityId);
    }



    //********************************    搜索页  -开始**************************************
    //设置分页
    public void setPageStartLen(SearchRequest searchPage_request) {
        if(searchPage_request.getPageNo()!=null && searchPage_request.getPageStepSize()!=null){
            int pageStartLen = (searchPage_request.getPageNo()-1) * searchPage_request.getPageStepSize();
            pageStartLen = pageStartLen < 0 ? 0 : pageStartLen;
            searchPage_request.setPageStartLen(pageStartLen);
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
        Map<Integer, StoreBase> storeId_StoreBaseMap = storeFeignClient.mapStoreBaseByStoreId(storeIdList);
        for (SearchCommodityVO commoditySearchVO : commoditySearchVOList)
            commoditySearchVO.setStoreBase( storeId_StoreBaseMap.get(commoditySearchVO.getStoreId()) );
    }
    //根据商品名字查询
    private SearchVO searchByCommodityName(SearchRequest searchPage_request) {
        int commodityCount = commodityMapper.countSearchByCommodityName(searchPage_request);
        if (0 == commodityCount)
            return null;
        List<SearchCommodityVO> commoditySearchVOList = commodityMapper.listSearchCommodityVOByCommodityName(searchPage_request);
        addStore(commoditySearchVOList);
        SearchVO searchPage = new SearchVO(commodityCount, commoditySearchVOList);
        return searchPage;
    }
    //根据种类的名字查询
    private SearchVO searchBySortName(SearchRequest searchPage_request) {
        Integer commodityCount = commodityMapper.countSearchBySortName(searchPage_request);
        if (0 == commodityCount)
            return null;
        List<SearchCommodityVO> commoditySearchVOList = commodityMapper.listSearchCommodityVOBySortName(searchPage_request);
        addStore(commoditySearchVOList);
        SearchVO searchPage = new SearchVO(commodityCount, commoditySearchVOList);
        return searchPage;
    }

    //根据店铺名字查询
    private SearchVO searchByStoreName(SearchRequest searchPage_request) {

        Map<Integer, StoreBase> storeId_StoreBaseMap = storeFeignClient.selStoreBaseMapByStoreName(searchPage_request.getSearchWord());
        if (0 == storeId_StoreBaseMap.size())
            return null;
        List<Integer> storeIdList = storeId_StoreBaseMap.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());

        int commodityCount = commodityMapper.countSearchByStoreId(storeIdList);
        if (0 == commodityCount)
            return null;

        List<SearchCommodityVO> commoditySearchVOList = commodityMapper.listSearchCommodityVOByStoreId(storeIdList, searchPage_request);
        for (SearchCommodityVO commoditySearchVO : commoditySearchVOList)
            commoditySearchVO.setStoreBase( storeId_StoreBaseMap.get(commoditySearchVO.getStoreId()) );

        SearchVO searchPage = new SearchVO(commodityCount, commoditySearchVOList);
        return searchPage;
    }

    @Override
    public SearchVO getSearchVO(SearchRequest searchPage_request) {
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

    @Override
    public CommodityToCart getCommodityToCart(Integer commodityId) {
        return commodityMapper.getCommodityToCart(commodityId);
    }


    //********************************    搜索页  -结束**************************************




    @Override
    public Map<Integer, CartCommodityVO> mapCartCommodityVO(List<Integer> commodityIdList) {
        return commodityMapper.mapCartCommodityVO(commodityIdList);
    }


    @Override
    public boolean isCommodityStoreEqualUser(int userId, int commodityId) {
        return 0 != commodityMapper.countCommodityStoreEqualUser(userId, commodityId);
    }

    @Override
    public ResultBean getCommodityToOrder(Integer userId, PurchaseDTO purchaseDTO) {
        CommodityToOrder commodityToOrder = commodityMapper.getCommodityToOrder(purchaseDTO.getCommodityId());

        if (commodityToOrder == null)
            return COMMODITY_NOT_FOUND;     //商品信息都找不到
        if (commodityToOrder.getCommodityStock() < purchaseDTO.getPurchaseQuantity())
            return COMMODITY_STOCK_INSUFFICIENT;  //商品库存不足
        if (commodityToOrder.getStoreId() == userId)
            return STORE_EQUAL_USER_ERROR;  //买家购买自己店铺的商品

        commodityMapper.subCommodityQuantity(purchaseDTO);
        commodityToOrder.setPurchaseQuantity(purchaseDTO.getPurchaseQuantity());

        return ResultFactory.createSuccessResult(commodityToOrder);
    }

    @Override
    public ResultBean listCommodityToOrder(Integer userId, List<PurchaseDTO> purchaseDTOList) {
        Map<Integer, CommodityToOrder> commodityId_commodityToOrder_map = commodityMapper.mapCommodityToOrder(purchaseDTOList);

        if (commodityId_commodityToOrder_map.size() != purchaseDTOList.size())
            return COMMODITY_NOT_FOUND;     //商品信息都找不到

        for (PurchaseDTO purchaseDTO : purchaseDTOList){
            CommodityToOrder commodityToOrder = commodityId_commodityToOrder_map.get(purchaseDTO.getCommodityId());

            if (commodityToOrder.getCommodityStock() < purchaseDTO.getPurchaseQuantity())
                return COMMODITY_STOCK_INSUFFICIENT;  //商品库存不足
            if (commodityToOrder.getStoreId() == userId)
                return STORE_EQUAL_USER_ERROR;  //买家购买自己店铺的商品

            commodityToOrder.setPurchaseQuantity(purchaseDTO.getPurchaseQuantity());
        }

        for (PurchaseDTO purchaseDTO :purchaseDTOList){
            commodityMapper.subCommodityQuantity(purchaseDTO);
        }

        List<CommodityToOrder> commodityToOrderList = commodityId_commodityToOrder_map.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());

        return ResultFactory.createSuccessResult(commodityToOrderList);
    }




    @Override
    public ResultBean subCommodityNumber(List<PurchaseDTO> purchaseDTOList) {
        for (PurchaseDTO purchaseDTO :purchaseDTOList){
            commodityMapper.subCommodityQuantity(purchaseDTO);
        }
        return successResult;
    }

    @Override
    public void addCommodityQuantity(PurchaseDTO purchaseDTO) {
        commodityMapper.addCommodityQuantity(purchaseDTO);
    }

    @Override
    public void addCommodityQuantityList(List<PurchaseDTO> purchaseDTOList) {
        for (PurchaseDTO purchaseDTO : purchaseDTOList){
            commodityMapper.addCommodityQuantity(purchaseDTO);
        }
    }

    @Override
    public List<StoreCommodityVO> listStoreCommodityVOByStoreId(int storeId) {
        return commodityMapper.listStoreCommodityVOByStoreId(storeId);
    }


}
