package com.cxp.shop_order.controller;


import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.StoreStatusFullVO;
import com.cxp.shop_order.eception.CommodityNotFound_exception;
import com.cxp.shop_order.eception.CommodityStockInsufficientException;
import com.cxp.shop_order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    static final ResultBean COMMODITY_NOT_FOUND = ResultFactory.createFailResult(ResultStatus.COMMODITY_NOT_FOUND);


    // 前端接口
    //提交订单
    @RequestMapping("/submitSingleOrderByUserId")
    public  ResultBean submitSingleOrderByUserId(Integer userId, @RequestBody OrderSon orderSon){
        try {
            int orderId = orderService.submitSingleOrder(userId, orderSon);

            List<Integer> orderIdList = new ArrayList<>();
            orderIdList.add(orderId);
            return payOrderByUserId(userId, orderIdList);
        } catch (CommodityStockInsufficientException e) {
            return COMMODITY_STOCK_INSUFFICIENT;
        } catch (CommodityNotFound_exception e) {
            return COMMODITY_NOT_FOUND;
        }
    }

    @RequestMapping("/submitMultipleOrderByUserId")
    public  ResultBean submitMultipleOrderByUserId(Integer userId, @RequestBody List<OrderSon> orderSonList){
        try {
            List<Integer> orderIdList = orderService.submitMultipleOrder(userId, orderSonList);
            return payOrderByUserId(userId, orderIdList);
        } catch (CommodityStockInsufficientException e) {
            return COMMODITY_STOCK_INSUFFICIENT;
        } catch (CommodityNotFound_exception e) {
            return COMMODITY_NOT_FOUND;
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
    @RequestMapping("/getStoreStatusFullVOByStoreId")
    public StoreStatusFullVO getStoreStatusFullVOByStoreId(Integer storeId){
        return orderService.getStoreStatusFullVO(storeId);
    }

    //前端接口
    //店铺中心 所有销售订单 总体内容
    @RequestMapping("/listStoreOrderParentRoughByStoreId")
    public List<OrderParent> listStoreOrderParentRoughByStoreId(Integer storeId){
        return orderService.listStoreOrderParentRough(storeId);
    }

    //前端接口
    //用户看自己所有 购物订单   粗略内容
    @RequestMapping("/listUserOrderParentRoughByUserId")
    public List<OrderParent> listUserOrderParentRoughByUserId(Integer userId, Integer orderState){
        return orderService.listUserOrderParentRough(userId, orderState);
    }


    //前端接口
    //店铺中心 所有某一个订单 详细内容
    @RequestMapping("/getStoreOrderParentByUserId")
    public OrderParent getStoreOrderParentByUserId(Integer userId, Integer orderId){
        return orderService.getStoreOrderParent(userId, orderId);
    }

    //前端接口
    //店铺中心 所有某一个订单 详细内容
    @RequestMapping("/getUserOrderParentByUserId")
    public OrderParent getUserOrderParentByUserId(Integer userId, Integer orderId){
        return orderService.getUserOrderParent(userId, orderId);
    }



}
