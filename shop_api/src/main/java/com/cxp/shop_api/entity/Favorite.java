package com.cxp.shop_api.entity;


import lombok.Data;

@Data
public class Favorite {

    int favorite_id;    //收藏夹id
    int userId;         //用户Id
    int commodityId;    //商品Id
}
