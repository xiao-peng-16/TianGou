package com.cxp.shop_zuul.service.feignClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "store")
public interface StoreFeignClient {


    //检测用户对应的 店铺id
    @RequestMapping(value = "selStoreIdByUserId",method = RequestMethod.GET)
    public Integer selStoreIdByUserId(@RequestParam  Integer userId);
}
