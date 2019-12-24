package com.cxp.shop_user.service.feignClient;


import com.cxp.shop_api.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "car")
public interface ShopCarFeignClient {

    //查看购物车数量
    @RequestMapping(value = "/selShopCarNumberByUserId",method = RequestMethod.GET)
    public int selShopCarNumberByUserId(@RequestParam Integer userId);

}
