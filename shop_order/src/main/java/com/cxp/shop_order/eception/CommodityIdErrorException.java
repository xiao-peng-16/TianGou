package com.cxp.shop_order.eception;

public class CommodityIdErrorException extends Exception {
    public CommodityIdErrorException(){
        super("商品id错误");
    }
}
