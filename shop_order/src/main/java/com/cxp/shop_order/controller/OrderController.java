package com.cxp.shop_order.controller;


import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.StoreStatusFullVO;
import com.cxp.shop_order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public  ResultBean submitSingleOrderByUserId(Integer userId, @RequestBody PurchaseDTO purchaseDTO){
        return orderService.submitSingleOrder(userId, purchaseDTO);
    }

    @RequestMapping("/submitMultipleOrderByUserId")
    public  ResultBean submitMultipleOrderByUserId(Integer userId, @RequestBody List<PurchaseDTO> purchaseDTOList){
        return orderService.submitMultipleOrder(userId, purchaseDTOList);
    }

    // 前端接口
    //支付订单
    @PostMapping("/payOrderByUserId")
    public ResultBean payOrderByUserId(Integer userId, @RequestBody List<Long> orderIdList){
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
    @RequestMapping("/listStoreOrderParentByStoreId")
    public List<OrderParent> listStoreOrderParentByStoreId(Integer storeId){
        return orderService.listStoreOrderParent(storeId);
    }

    //前端接口
    //用户看自己所有 购物订单   粗略内容
    @RequestMapping("/listUserOrderParentByUserId")
    public List<OrderParent> listUserOrderParentByUserId(Integer userId, Integer orderState){
        return orderService.listUserOrderParent(userId, orderState);
    }


    //没用到 备用
    //某一个订单 详细内容
    @RequestMapping("/getOrderParent")
    public OrderParent getOrderParent(Integer orderId){
        return orderService.getOrderParent(orderId);
    }




}
