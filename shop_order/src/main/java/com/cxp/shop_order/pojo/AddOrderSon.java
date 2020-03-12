package com.cxp.shop_order.pojo;


import lombok.Data;

@Data
public class AddOrderSon {

    long orderId;


    int commodityId;
    int storeId;
    double commodityPrice;
    int commodityStock;
    String commodityName;
    String commodityPhoto;
    int purchaseQuantity;

//    CommodityToOrder commodityToOrder;
}
