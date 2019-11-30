package com.cxp.shop_api.entity;

import lombok.Data;

@Data
public class Store {
    int storeId;
    String storeName;
    String storeProvince; //省
    String storeCity;     //市
    float storeDescribe;
    float storeAttitude;
    float storeDeliverySpeed;

}
