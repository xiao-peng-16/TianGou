package com.cxp.shop_api.entity;

import lombok.Data;
import lombok.experimental.Accessors;



@Data
public class OrderSon {

    int commodityId;            //商品id
    int purchaseQuantity;       //数量
    double commodityPrice;      //商品单价
    String commodityName;       //商品名
    String commodityPhoto;      //商品图片
}
