package com.cxp.shop_zuul.exception;

public class UserIdLoginOverdueException extends Exception{
    public UserIdLoginOverdueException(){
        super("用户没注册或登录过期");
    }
}
