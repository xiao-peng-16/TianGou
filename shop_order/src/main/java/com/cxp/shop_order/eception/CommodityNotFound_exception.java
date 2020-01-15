package com.cxp.shop_order.eception;

public class CommodityNotFound_exception extends Exception {
    public CommodityNotFound_exception(){
        super("商品id错误");
    }
}
