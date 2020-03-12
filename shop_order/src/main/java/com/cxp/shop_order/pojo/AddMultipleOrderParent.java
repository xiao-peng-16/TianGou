package com.cxp.shop_order.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddMultipleOrderParent {



    long orderId;                 //订单的id
    int storeId;                 //店铺id
    String storeName;            //店铺用户名
    int orderTotalQuantity;      // 订单总数量'
    double orderTotalPrice;       // 订单总金额

}
