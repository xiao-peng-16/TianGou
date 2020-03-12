package com.cxp.shop_order.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "store")
public interface StoreFeignClient {


    //查询一组店铺名 根据id
    @RequestMapping(value = "/mapStoreNameByStoreId", method = RequestMethod.POST)
    public Map<Integer,String> mapStoreNameByStoreId(List<Integer> storeIdList);

    //查询店铺名 根据id
    @RequestMapping(value = "/getStoreNameByStoreId", method = RequestMethod.GET)
    public String  getStoreNameByStoreId(@RequestParam("storeId") Integer storeId);


    //查询用户id 根据店铺id
    @RequestMapping(value = "/mapUserIdByStoreId", method = RequestMethod.POST)
    public Map<Integer,Integer> mapUserIdByStoreId(List<Integer> storeIdList);
}
