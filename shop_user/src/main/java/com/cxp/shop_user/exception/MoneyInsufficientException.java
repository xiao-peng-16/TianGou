package com.cxp.shop_user.exception;

public class MoneyInsufficientException extends Exception{
    public MoneyInsufficientException() {
        super("买家余额不足");
    }
}