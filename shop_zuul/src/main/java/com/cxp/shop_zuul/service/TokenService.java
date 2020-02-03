package com.cxp.shop_zuul.service;


import com.cxp.shop_zuul.exception.StoreNotRegisterException;
import com.cxp.shop_zuul.exception.UserIdLoginOverdueException;
import com.cxp.shop_zuul.service.feignClient.StoreFeignClient;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TokenService {

    @Resource
    ValueOperations valueOperations;
    @Autowired
    StoreFeignClient storeFeignClient;


    @Resource
    String USER_ID_LOGIN_OVERDUE;
    @Resource
    String STORE_NOT_REGISTER;

    static final String KEY_NAME = "token";
    static final String ByUserId_TARGET_URI  = "ByUserId";
    static final String ByStoreId_TARGET_URI = "ByStoreId";


    static final String KEY_TOKEN = "token";
    static final String KEY_USER_ID = "userId";
    static final String KEY_STORE_ID = "storeId";

    static final long TOKEN_REFRESH_TIME =  10  * 60 * 1000;
    static final long TOKEN_EXPIRED_TIME =  30  * 60 * 1000;


    static final UserIdLoginOverdueException USER_ID_LOGIN_OVERDUE_EXCEPTION = new UserIdLoginOverdueException();
    static final StoreNotRegisterException STORE_NOT_REGISTER_EXCEPTION = new StoreNotRegisterException();

    //用户登录 将userId 转为token
    public abstract String getUserLoginToken(Integer userId);
    //解析userId 并 刷新
    public abstract String parseUserId(RequestContext ctx) throws UserIdLoginOverdueException;
    //解析storeId 并 刷新
    public abstract String parseStoreId(RequestContext ctx) throws UserIdLoginOverdueException, StoreNotRegisterException;

    public boolean shouldFilter(RequestContext ctx) {

        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();   //获取请求的uri


        try {

            if (requestURI.endsWith(ByUserId_TARGET_URI)){
                //解析并刷新出userId
                addRequestQueryParams(ctx, "userId", parseUserId(ctx));
            }else if (requestURI.endsWith(ByStoreId_TARGET_URI)){
                //解析并刷新出userId
                addRequestQueryParams(ctx, "storeId", parseStoreId(ctx));
            }
        } catch (UserIdLoginOverdueException e) {
            ctx.setResponseBody(USER_ID_LOGIN_OVERDUE);
            return true;
        } catch (StoreNotRegisterException e) {
            ctx.setResponseBody(STORE_NOT_REGISTER);
            return true;
        }

        return false;
    }



    private void addRequestQueryParams(RequestContext ctx, String key, String value){
        ctx.getRequest().getParameterMap();    //必须加这句 不然ctx.getRequestQueryParams()失败
        Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();//参数键值对map
//                如果一开始请求头里没参数 那么requestQueryParams为空
        if (requestQueryParams==null) requestQueryParams=new HashMap<>();

        //为了提高性能 ，其他参数都放请求体了，用post请求
//                Map<String, List<String>> requestQueryParams=new HashMap<>();
        List<String> arrayList = new ArrayList<>();
        arrayList.add(value);      //添加 对应的values
        requestQueryParams.put(key, arrayList); //把参数键值对放入map
        ctx.setRequestQueryParams(requestQueryParams);  //重新设置请求头所含有的参数
    }
}
