package com.cxp.shop_commodity.service.impl;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.Sort;
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

        CommodityPhotoVideo commodityPhotoVideo = commodityMapper.selCommodityPhotoVideo(commodity.getCommodityId());

        if (0 == commodityMapper.updCommodity(commodity))
            return COMMODITY_UPDATE_ERROR;

        ArrayList<String> urlList = new ArrayList<>();
        String oldCommodityPhoto = commodityPhotoVideo.getCommodityPhoto();
        String newCommodityPhoto = commodity.getCommodityPhoto();

        String oldCommodityVideo = commodityPhotoVideo.getCommodityVideo();
        String newCommodityVideo = commodity.getCommodityVideo();

        if (null != oldCommodityPhoto && !oldCommodityPhoto.equals(newCommodityPhoto))
            urlList.add(oldCommodityPhoto);
        if (null != oldCommodityVideo && !oldCommodityVideo.equals(newCommodityVideo))
            urlList.add(oldCommodityVideo);

        if (0 != urlList.size())
            imagesFeignClient.dlelQiniuImagesList(urlList);

        return successResult;
    }

    @Override
    public ResultBean updCommodityOnShelves(Integer storeId, Integer commodityId, Boolean commodityOnShelves) {
        if (null == commodityId || null == commodityOnShelves)
            return COMMODITY_UPDATE_ERROR;
        return 0 != commodityMapper.updCommodityOnShelves(storeId, commodityId, commodityOnShelves)
                ? successResult : COMMODITY_UPDATE_ERROR;
    }

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




    @Override
    public Map<Integer, CommodityToOrder> mapCommodityToOrder(List<Integer> commodityIdList){
        return commodityMapper.mapCommodityToOrder(commodityIdList);
    }

    @Override
    public ResultBean updCommodityNumber(List<CommodityNumberChange> commodityNumberChangeList) {
        for (CommodityNumberChange commodityNumberChange :commodityNumberChangeList){
            commodityMapper.updCommodityNumber(commodityNumberChange);
        }
        return successResult;
    }

    @Override
    public List<StoreCommodityVO> selStoreCommodityVOByStoreId(int storeId) {
        return commodityMapper.selStoreCommodityVOByStoreId(storeId);
    }

    @Override
    public Commodity selCommodityByCommodityId(int commodityId) {
        return commodityMapper.selCommodityByCommodityId(commodityId);
    }

}
