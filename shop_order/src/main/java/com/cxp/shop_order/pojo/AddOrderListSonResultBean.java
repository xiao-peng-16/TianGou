package com.cxp.shop_order.pojo;

import lombok.Data;

import java.util.List;

@Data
public class AddOrderListSonResultBean {

    private boolean success;
    private int status;
    private List<AddOrderSon> data;
}
