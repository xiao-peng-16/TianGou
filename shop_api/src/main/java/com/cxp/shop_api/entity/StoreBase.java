package com.cxp.shop_api.entity;

import lombok.Data;

@Data
public class StoreBase {

    int storeId;
    String storeName;
    int cityCode; //省市区代码

}
