package com.cxp.shop_api.dto;


import lombok.Data;

@Data
public class UserOpenStoreDTO {

    int userId;         //用户id
    String storeName;   //店铺名字
    String storeProvince; //省
    String storeCity;     //市
}
