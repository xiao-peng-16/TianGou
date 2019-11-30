package com.cxp.shop_order.eception;

public class AddOrderException extends Exception {
    public AddOrderException(){
        super("添加订单错误");
    }
}
