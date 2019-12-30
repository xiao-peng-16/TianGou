package com.cxp.shop_commodity.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "images")
public interface ImagesFeignClient {

    //删除图片
    @RequestMapping(value = "/dlelQiniuImages",method = RequestMethod.GET)
    public void  dlelQiniuImages(@RequestParam String imagesURl);

    //删除一组图片
    @RequestMapping(value = "/dlelQiniuImagesList",method = RequestMethod.POST)
    public void  dlelQiniuImagesList(List<String> imagesURlList);
}
