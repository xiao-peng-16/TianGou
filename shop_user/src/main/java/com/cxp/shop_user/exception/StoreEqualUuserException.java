package com.cxp.shop_user.exception;

public class StoreEqualUuserException extends Exception {
    public StoreEqualUuserException(){
        super("用户买自己店铺的东西");
    }
}
