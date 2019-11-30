package com.cxp.shop_commodity.service.FeignClient;


import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "order")
public interface OrderFeignClient {

    //添加订单
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    public ResultBean addOrder(OrderParent orderParent);
}