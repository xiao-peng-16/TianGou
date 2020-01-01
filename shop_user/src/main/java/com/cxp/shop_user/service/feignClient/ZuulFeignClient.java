package com.cxp.shop_user.service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "zuul")
public interface ZuulFeignClient {

    @RequestMapping(value = "/getUserLoginToken",method = RequestMethod.GET)
    public String getUserLoginToken(@RequestParam Integer userId);
}
