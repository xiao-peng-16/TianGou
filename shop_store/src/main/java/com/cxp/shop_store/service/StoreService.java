package com.cxp.shop_store.service;

import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.entity.Store;

import java.util.List;
import java.util.Map;

public interface StoreService {

    //返回店铺完整信息   用于组成店铺页的店铺信息
    public Store selStoreByStoreId(Integer userId);

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息
    public Map<Integer,StoreToCommodity> mapStoreToCommodityByStoreId(List<Integer> storeIdList);

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺名字 模糊查询
    public Map<Integer,StoreToCommodity> selStoreToCommodityMapByStoreName(String searchWord);

}
