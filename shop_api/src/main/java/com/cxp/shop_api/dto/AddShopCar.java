package com.cxp.shop_api.dto;

/*
 *       前后端交互 添加购物车  比ShopCar 少了 shopCarID
 *       通过Token获取userId组成ShopCar
 *       保证安全性，减少数据冗余
 * */

import lombok.Data;

@Data
public class AddShopCar {
    int userId;         //用户id
    int commodityId;    //商品id
    int purchaseQuantity;   //数量
}
