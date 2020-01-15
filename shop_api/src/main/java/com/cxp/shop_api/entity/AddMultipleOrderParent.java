package com.cxp.shop_api.entity;

import com.cxp.shop_api.vo.OrderCommodityVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddMultipleOrderParent {


    //数据库字段
    int orderId;                 //订单的id
    int userId;                  //用户id
    int storeId;                 //卖家id
    String orderTime;            //'时间'
    int orderTotalQuantity;      // 订单总数量'
    double orderTotalPrice;       // 订单总金额

    List<OrderSon> orderSonList;//该订单每个内容




    public AddMultipleOrderParent(int userId, int storeId, String orderTime) {
        this.userId = userId;
        this.storeId = storeId;
        this.orderTime = orderTime;
    }


}
