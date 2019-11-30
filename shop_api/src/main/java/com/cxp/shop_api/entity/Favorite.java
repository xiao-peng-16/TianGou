package com.cxp.shop_api.entity;


import lombok.Data;

@Data
public class Favorite {

    int userId;         //用户Id
    int commodityId;    //商品Id
    String orderTime;   //时间 用于排序

}
