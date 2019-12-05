package com.cxp.shop_order.controller;


import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_order.eception.AddOrderException;
import com.cxp.shop_order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/*
 *       订单
 * */

@CrossOrigin(allowCredentials = "true")
@RestController
public class OrderController {

    @Autowired
    OrderServiceImpl orderService;

    static final ResultBean ORDER_Add_ERROR = ResultFactory.createFailResult(ResultStatus.ORDER_Add_ERROR);


    //添加订单
    @RequestMapping("/addOrder")
    public  ResultBean addOrder(@RequestBody OrderParent orderParent){
        try {
            return orderService.addOrder(orderParent);
        } catch (AddOrderException e) {
            return ORDER_Add_ERROR;
        }
    }

    // 前端接口
    //支付订单
    @RequestMapping("/payOrderByUserId")
    public ResultBean payOrderByUserId(Integer userId, Integer orderId){
        return orderService.payOrderByUserId(userId, orderId);
    }

    // 前端接口
    //查询店铺状态
    @RequestMapping("/selStoreStatusFullVOByUserId")
    public ResultBean selStoreStatusFullVOByUserId(Integer userId){
        return ResultFactory.createSuccessResult(orderService.selStoreStatusFullVO(userId));
    }

    //前端接口
    //店铺中心 所有销售订单 总体内容
    @RequestMapping("/selStoreOrderGeneraVOByUserId")
    public ResultBean selStoreOrderGeneraVOByUserId(Integer userId){
        return ResultFactory.createSuccessResult(orderService.listStoreOrderGeneraVO(userId));
    }

    //前端接口
    //店铺中心 所有某一个订单 详细内容
    @RequestMapping("/selStoreOrderParentByUserId")
    public ResultBean selStoreOrderParentByUserId(Integer userId, Integer orderId){
        return ResultFactory.createSuccessResult(orderService.selStoreOrderParent(userId, orderId));
    }


    //前端接口
    //用户看自己所有 购物订单   总体内容
//    @RequestMapping("/listUserOrderGeneraVOByUserId")
//    public ResultBean listUserOrderGeneraVOByUserId(Integer userId){
//        return ResultFactory.createSuccessResult(orderService.listUserOrderGeneraVO(userId));
//    }
}
