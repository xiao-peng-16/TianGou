package com.cxp.shop_order.eception;

public class CommodityStockInsufficientException extends Exception {
    public CommodityStockInsufficientException(){
        super("商品库存不足");
    }
}
