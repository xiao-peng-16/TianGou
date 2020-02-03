package com.cxp.shop_api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class OrderParent {

    final static int WAIT_PAYMENT = 0;   //待付款
    final static int WAIT_SHIPMENTS = 1; //待发货
    final static int WAIT_RECEIVING = 2; //待收货
    final static int WAIT_EVALUATED = 2; //待评价


    //数据库字段
    long orderId;                 //订单的id
    int userId;                  //用户id
    String userName;             //买家用户名
    int storeId;                 //店铺id
    String storeName;            //店铺用户名
    String orderTime;            //'时间'
    int orderTotalQuantity;      // 订单总数量'
    double orderTotalPrice;       // 订单总金额
    int orderState;            //订单状态 待付款 待发货 待评价

    List<OrderSon> orderSonList;//该订单每个内容

}
