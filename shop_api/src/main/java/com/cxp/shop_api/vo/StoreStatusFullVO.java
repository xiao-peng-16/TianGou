package com.cxp.shop_api.vo;

import lombok.Data;

@Data
public class StoreStatusFullVO {
    StoreStatusBeanVO currentMonth; //本月
    StoreStatusBeanVO totality; //全部


}
