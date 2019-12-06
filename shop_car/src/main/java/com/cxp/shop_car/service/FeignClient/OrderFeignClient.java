package com.cxp.shop_car.service.FeignClient;


import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.entity.ShopCar;
import com.cxp.shop_api.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@FeignClient(value = "order")
public interface OrderFeignClient {

    //提交订单
    @RequestMapping("/submitOrderByUserId")
    public ResultBean submitOrderByUserId(@RequestParam Integer userId, List<ShopCar> shopCarList);
}
