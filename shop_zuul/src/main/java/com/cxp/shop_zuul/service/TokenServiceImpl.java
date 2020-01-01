package com.cxp.shop_zuul.service;


import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_zuul.exception.StoreNotRegisterException;
import com.cxp.shop_zuul.exception.UserIdLoginOverdueException;
import com.cxp.shop_zuul.pojo.TokenTarget;
import com.cxp.shop_zuul.service.feignClient.StoreFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    ValueOperations valueOperations;
    @Autowired
    StoreFeignClient storeFeignClient;


    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean USER_ID_LOGIN_OVERDUE = ResultFactory.createFailResult(ResultStatus.USER_ID_LOGIN_OVERDUE);
    static final ResultBean STORE_NOT_REGISTER = ResultFactory.createFailResult(ResultStatus.STORE_NOT_REGISTER);



    static final UserIdLoginOverdueException USER_ID_LOGIN_OVERDUE_EXCEPTION = new UserIdLoginOverdueException();
    static final StoreNotRegisterException STORE_NOT_REGISTER_EXCEPTION = new StoreNotRegisterException();

    private static final int TIME = 30;
    private static final String KEY_NAME = "token";

    //添加    保存为String 方便zuul过滤器 设置请求头参数
    public  String getUserLoginToken(Integer userId){
        String token = UUID.randomUUID().toString();
        valueOperations.set(token, new TokenTarget(userId), TIME, TimeUnit.MINUTES);
        return token;
    }
    //删除
    public ResultBean deleteToken(HttpServletRequest request){
        String token = request.getHeader(KEY_NAME);
        if (token!=null){
            redisTemplate.delete(token);
        }
        return  successResult;
    }

    //解析userId 并 刷新
    public Integer parseUserId(HttpServletRequest request){
        String token = request.getHeader(KEY_NAME);
        if (null!=token && redisTemplate.expire(token, TIME, TimeUnit.MINUTES))
            return ((TokenTarget)this.valueOperations.get(token)).getUserId();
        else return null;
    }

    //设置 storeId
    public ResultBean serStoreId(HttpServletRequest request) throws Exception{
        String token = request.getHeader(KEY_NAME);
        if (null == token)
            return USER_ID_LOGIN_OVERDUE;
        TokenTarget tokenTarget = (TokenTarget) this.valueOperations.get(token);

        Integer storeId = storeFeignClient.selStoreIdByUserId(tokenTarget.getUserId());
        if (null != storeId){
            tokenTarget.setStoreId(storeId);
            valueOperations.set(token, tokenTarget, TIME, TimeUnit.MINUTES);
            return successResult;
        }else
            return STORE_NOT_REGISTER;

    }


    //解析store 找不到去店铺微服务找 并 刷新
    public Integer parseStoreId(HttpServletRequest request) throws UserIdLoginOverdueException, StoreNotRegisterException {
        String token = request.getHeader(KEY_NAME);
        if (null == token)
            throw USER_ID_LOGIN_OVERDUE_EXCEPTION;

        TokenTarget tokenTarget = (TokenTarget) this.valueOperations.get(token);
        if (null == tokenTarget)
            throw USER_ID_LOGIN_OVERDUE_EXCEPTION;

        if (null != tokenTarget.getStoreId()){
            if (redisTemplate.expire(token, TIME, TimeUnit.MINUTES)){
//                System.out.println("第二次 直接找到storeId了");
                return tokenTarget.getStoreId();
            }else
                throw USER_ID_LOGIN_OVERDUE_EXCEPTION;
        }else {
            Integer storeId = storeFeignClient.selStoreIdByUserId(tokenTarget.getUserId());
            if (null != storeId){
                tokenTarget.setStoreId(storeId);
                valueOperations.set(token, tokenTarget, TIME, TimeUnit.MINUTES);
//                System.out.println("第一次 找不到去店铺微服务找 找到了");
                return tokenTarget.getStoreId();
            }else
                throw STORE_NOT_REGISTER_EXCEPTION;
        }



    }
}
