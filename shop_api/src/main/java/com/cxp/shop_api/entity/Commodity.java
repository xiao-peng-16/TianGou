package com.cxp.shop_api.entity;


import lombok.Data;





@Data
public class Commodity {
    String commodityName;       //商品名
    int commodityId;

    int storeId;
    Store store;        //商品所属店

    int sortId;
    String sortName;

    String commodityDescribe;   //商品描述
    boolean baoYou;     //是否包邮
    String commodityPhoto;  //商品图片
    String commodityVideo;   //商品视频'
    double commodityPrice;      //价格

    int commoditySales;   //销量
    int commodityPopularity;    //人气 (点击量)
    int commodityStock;         //库存

    boolean commodityOnShelves; //是否处于上架状态
}
