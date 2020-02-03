package com.cxp.shop_order.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AddMultipleOrderParent {



    long orderId;                 //订单的id
    String orderTime;            //'时间'
    int storeId;                 //店铺id
    String storeName;            //店铺用户名
    int orderTotalQuantity;      // 订单总数量'
    double orderTotalPrice;       // 订单总金额

    List<AddOrderSon> addOrderSonList;//该订单每个内容

}
