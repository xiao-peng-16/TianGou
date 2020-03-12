package com.cxp.shop_api.entity;

import lombok.Data;

@Data
public class StoreBase {

    int storeId;
    String storeName;
    String cityCode; //省市区代码

}
