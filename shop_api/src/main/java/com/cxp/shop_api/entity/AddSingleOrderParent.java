package com.cxp.shop_api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddSingleOrderParent {


    //数据库字段
    int orderId;                 //订单的id
    int userId;                  //用户id
    int storeId;                 //卖家id
    int orderTotalQuantity;      // 订单总数量'
    double orderTotalPrice;       // 订单总金额


    public AddSingleOrderParent(int userId, int storeId) {
        this.userId = userId;
        this.storeId = storeId;
    }
}
