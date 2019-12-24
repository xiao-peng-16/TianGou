package com.cxp.shop_api.entity;

import lombok.Data;

@Data
public class User {

    int userId;
    String userName;
    String userPassword;
    String userPhoto;
    double userMoney;

    //非数据库字段
    int shopCarNumber; //购物车数量


}
