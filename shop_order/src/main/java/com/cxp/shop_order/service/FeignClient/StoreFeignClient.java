package com.cxp.shop_order.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "store")
public interface StoreFeignClient {


    //查询一组店铺名 根据id
    @RequestMapping(value = "/mapStoreNameByStoreId", method = RequestMethod.POST)
    public Map<Integer,String> mapStoreNameByStoreId(List<Integer> storeIdList);

    //查询店铺名 根据id
    @RequestMapping(value = "/selStoreNameByStoreId", method = RequestMethod.GET)
    public String  selStoreNameByStoreId(@RequestParam("storeId") Integer storeId);
}
