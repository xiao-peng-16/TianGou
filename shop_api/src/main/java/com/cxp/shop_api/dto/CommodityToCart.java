package com.cxp.shop_api.dto;


import lombok.Data;

@Data
public class CommodityToCart {

    String commodityName;       //商品名
    String commodityDescribe;   //商品描述
    double commodityPrice;      //价格
    boolean commodityOnShelves; //是否处于上架状态
    String commodityPhoto;      //商品图片
}
