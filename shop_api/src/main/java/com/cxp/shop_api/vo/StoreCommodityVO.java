package com.cxp.shop_api.vo;

import lombok.Data;

@Data
public class StoreCommodityVO {

    int commodityId;            //商品id
    String commodityName;       //商品名
    String commodityPhoto;  //商品图片
    double commodityPrice;      //价格
    int commoditySales;         //销量
    int commodityStock;         //库存
}
