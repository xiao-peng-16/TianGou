package com.cxp.shop_api.dto;

import lombok.Data;

@Data
public class UserOpenStore {

    int userId;
    String storeName;
    String cityCode; //省市区代码

}
