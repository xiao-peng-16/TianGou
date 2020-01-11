package com.cxp.shop_api.entity;

import lombok.Data;

@Data
public class User {

    int userId;
    String userName;
    String userPassword;
    String userPhoto;
    double userMoney;

}
