package com.cxp.shop_order.controller;


import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_order.eception.CommodityIdErrorException;
import com.cxp.shop_order.eception.CommodityStockInsufficientException;
import com.cxp.shop_order.eception.StoreEqualUserErrorException;
import com.cxp.shop_order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;


/*
 *       订单
 * */

@CrossOrigin(allowCredentials = "true")
@RestController
public class OrderController {

    @Autowired
    OrderServiceImpl orderService;

    static final ResultBean COMMODITY_STOCK_INSUFFICIENT = ResultFactory.createFailResult(ResultStatus.COMMODITY_STOCK_INSUFFICIENT);
    static final ResultBean COMMODITY_ID_ERROR = ResultFactory.createFailResult(ResultStatus.COMMODITY_ID_ERROR);
    static final ResultBean STORE_EQUAL_USER_ERROR = ResultFactory.createFailResult(ResultStatus.STORE_EQUAL_USER_ERROR);


    // 前端接口
    //提交订单
    @RequestMapping("/submitOrderByUserId")
    public  ResultBean submitOrderByUserId(Integer userId, @RequestBody LinkedList<OrderSon> orderSonList){
        try {
            List<Integer> orderIdList = orderService.submitOrder(userId, orderSonList);
            return payOrderByUserId(userId, orderIdList);
        } catch (CommodityStockInsufficientException e) {
            return COMMODITY_STOCK_INSUFFICIENT;
        } catch (CommodityIdErrorException e) {
            return COMMODITY_ID_ERROR;
        } catch (StoreEqualUserErrorException e) {
            return STORE_EQUAL_USER_ERROR;
        }
    }

    // 前端接口
    //支付订单
    @PostMapping("/payOrderByUserId")
    public ResultBean payOrderByUserId(Integer userId, @RequestBody List<Integer> orderIdList){
        return orderService.payOrderByUserId(userId, orderIdList);
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
