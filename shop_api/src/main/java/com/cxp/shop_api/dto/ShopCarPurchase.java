package com.cxp.shop_api.dto;


import lombok.Data;

@Data
public class ShopCarPurchase {
    int userId;             //用户id
    int commodityId;        //商品id
    int purchaseQuantity;   //数量
}
