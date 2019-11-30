package com.cxp.shop_api.entity;


import lombok.Data;





@Data
public class Commodity {
    String commodityName;       //商品名
    int commodityId;

    int storeId;
    Store store;        //商品所属店
    Sort sort; //商品所属种类
    String commodityDescribe;   //商品描述
    Boolean baoYou;     //是否包邮
    String commodityPhotoname;  //商品图片
    String commodityVideoname;   //商品视频'
    double commodityPrice;      //价格

    int commoditySales;   //销量
    int commodityPopularity;    //人气 (点击量)
    int commodityStock;         //库存

}
