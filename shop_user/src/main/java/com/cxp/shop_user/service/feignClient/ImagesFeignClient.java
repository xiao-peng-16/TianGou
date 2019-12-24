package com.cxp.shop_user.service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "images")
public interface ImagesFeignClient {

    //删除图片
    @RequestMapping(value = "/dlelQiniuImages",method = RequestMethod.GET)
    public void  dlelQiniuImages(@RequestParam String imagesURl);
}
