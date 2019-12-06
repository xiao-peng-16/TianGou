package com.cxp.shop_api.entity;

import com.cxp.shop_api.vo.OrderCommodityVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderParent {

    final static int WAIT_PAYMENT = 0;   //待付款
    final static int WAIT_SHIPMENTS = 1; //待发货
    final static int WAIT_RECEIVING = 2; //待收货
    final static int WAIT_EVALUATED = 2; //待评价

    //非数据库该表字段
    String userName;    //买家用户名
    String storeName;   //卖家用户名

    //数据库字段
    int orderId;                //订单的id
    int userId;                 //用户id
    int storeId;               //卖家id
    String orderTime;           //'时间'
    int orderSumNumber;       // 订单总数量'
    double orderSumPrice;     // 订单总金额
    int orderState;            //订单状态 待付款 待发货 待评价

    //这2 条不同时出现
    List<OrderSon> orderSonList;//该订单每个内容

    public OrderParent(int userId, int storeId, String orderTime) {
        this.userId = userId;
        this.storeId = storeId;
        this.orderTime = orderTime;
    }

    List<OrderCommodityVO> orderCommodityVOList;//商品简单信息列表 ， orderSonList里有OrderCommodityVO 两者不重复出现


}
