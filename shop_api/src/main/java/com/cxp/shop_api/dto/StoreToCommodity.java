package com.cxp.shop_api.dto;

import lombok.Data;

@Data
public class StoreToCommodity {

    int storeId;
    String storeName;
    String storeProvince; //省
    String storeCity;     //市

}
