package com.cxp.shop_order.eception;

public class StoreEqualUserErrorException extends Exception {
    public StoreEqualUserErrorException(){
        super("买家购买自己商品");
    }
}
