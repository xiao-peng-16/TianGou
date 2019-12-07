package com.cxp.shop_order.controller;


import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_order.eception.AddOrderException;
import com.cxp.shop_order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;


/*
 *       订单
 * */

@CrossOrigin(allowCredentials = "true")
@RestController
public class OrderController {

    @Autowired
    OrderServiceImpl orderService;

    static final ResultBean ORDER_Add_ERROR = ResultFactory.createFailResult(ResultStatus.ORDER_Add_ERROR);

    // 前端接口
    //提交订单
    @RequestMapping("/submitOrderByUserId")
    public  ResultBean submitOrderByUserId(Integer userId, @RequestBody LinkedList<OrderSon> orderSonList){
        try {
            return orderService.submitOrder( userId,  orderSonList);
        } catch (AddOrderException e) {
            return ORDER_Add_ERROR;
        }
    }

    // 前端接口
    //支付订单
    @RequestMapping("/payOrderByUserId")
    public ResultBean payOrderByUserId(Integer userId, String orderTime){
        return orderService.payOrderByUserId(userId, orderTime);
    }

    // 前端接口
    //查询店铺状态
    @RequestMapping("/selStoreStatusFullVOByUserId")
    public ResultBean selStoreStatusFullVOByUserId(Integer userId){
        return ResultFactory.createSuccessResult(orderService.selStoreStatusFullVO(userId));
    }

    //前端接口
    //店铺中心 所有销售订单 总体内容
    @RequestMapping("/listStoreOrderParentRoughByUserId")
    public ResultBean listStoreOrderParentRoughByUserId(Integer userId){
        return ResultFactory.createSuccessResult(orderService.listStoreOrderParentRough(userId));
    }

    //前端接口
    //用户看自己所有 购物订单   粗略内容
    @RequestMapping("/listUserOrderParentRoughByUserId")
    public ResultBean listUserOrderParentRoughByUserId(Integer userId, Integer orderState){
        return ResultFactory.createSuccessResult(orderService.listUserOrderParentRough(userId, orderState));
    }


    //前端接口
    //店铺中心 所有某一个订单 详细内容
    @RequestMapping("/selStoreOrderParentByUserId")
    public ResultBean selStoreOrderParentByUserId(Integer userId, Integer orderId){
        return ResultFactory.createSuccessResult(orderService.selStoreOrderParent(userId, orderId));
    }

    //前端接口
    //店铺中心 所有某一个订单 详细内容
    @RequestMapping("/selUserOrderParentByUserId")
    public ResultBean selUserOrderParentByUserId(Integer userId, Integer orderId){
        return ResultFactory.createSuccessResult(orderService.selUserOrderParent(userId, orderId));
    }



}
