package com.cxp.shop_api.vo;

import lombok.Data;

@Data
public class CartCommodityVO {

    int commodityId;            //商品id
    double commodityPrice;      //价格
    int commodityStock;         //库存
    Boolean baoYou;             //是否包邮
    boolean commodityOnShelves; //是否处于上架状态
}
