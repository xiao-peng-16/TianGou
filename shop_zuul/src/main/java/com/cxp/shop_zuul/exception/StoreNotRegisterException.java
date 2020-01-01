package com.cxp.shop_zuul.exception;

public class StoreNotRegisterException extends Exception{
    public StoreNotRegisterException(){
        super("店铺未注册");
    }
}
