package com.cxp.shop_api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderParent {

    //非数据库该表字段
    String userName;    //卖家用户名

    //数据库字段
    int orderId;        //订单的id
    int userId;         //用户id
    String orderTime;   //'时间'
    boolean orderSubmit;//是否已支付
    List<OrderSon> orderSonList;    //订单内容 用于和微服务shop_order 交互




    public OrderParent(int userId) {
        this.userId = userId;
    }
}
