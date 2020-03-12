package com.cxp.shop_order.pojo;


import lombok.Data;

@Data
public class OrderPayPO {
    int storeId;
    int userId;
    double moneyChange;
    boolean stockLockState;
}
