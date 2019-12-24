package com.cxp.shop_images.service.feignClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "user")
public interface UserFeignClient {

    //查询头像
    @RequestMapping(value = "/selUserPhotoByUserId",method = RequestMethod.GET)
    public String  selUserPhotoByUserId(@RequestParam Integer userId);
}
