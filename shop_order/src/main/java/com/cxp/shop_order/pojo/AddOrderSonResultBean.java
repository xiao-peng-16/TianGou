package com.cxp.shop_order.pojo;

import lombok.Data;

@Data
public class AddOrderSonResultBean {
    private boolean success;
    private int status;
    private AddOrderSon data;
}
