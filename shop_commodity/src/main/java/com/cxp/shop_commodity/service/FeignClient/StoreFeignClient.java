package com.cxp.shop_commodity.service.FeignClient;

import com.cxp.shop_api.entity.Store;
import com.cxp.shop_api.entity.StoreBase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "store")
public interface StoreFeignClient {


    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺名字 模糊查询
    @RequestMapping(value = "selStoreBaseMapByStoreName", method = RequestMethod.GET)
    public Map<Integer, StoreBase> selStoreBaseMapByStoreName(@RequestParam("searchWord") String searchWord);

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺id
    @RequestMapping(value = "mapStoreBaseByStoreId", method = RequestMethod.POST)
    public Map<Integer,StoreBase> mapStoreBaseByStoreId(List<Integer> storeIdList);


    //返回店铺完整信息   用于组成店铺页 和 商品页的店铺信息
    @RequestMapping(value = "getStoreByStoreId", method = RequestMethod.GET)
    public Store getStoreByStoreId(@RequestParam("storeId") Integer storeId);


}
