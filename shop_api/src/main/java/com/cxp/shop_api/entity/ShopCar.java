package com.cxp.shop_api.entity;


import lombok.Data;




@Data
public class ShopCar {

    int shopCarId;      //买家加入购物车 的编号
    int userId;         //用户id
    int commodityId;    //商品id
    int chooseNumber;   //数量

}


