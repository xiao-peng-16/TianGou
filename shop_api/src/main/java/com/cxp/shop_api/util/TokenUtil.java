package com.cxp.shop_api.util;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.HttpServletRequest;
import java.time.format.SignStyle;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class TokenUtil {

    RedisTemplate redisTemplate;
    ValueOperations valueOperations;

    private static final int TIME = 30;
    private static final String NAME = "token";

    //添加    保存为String 方便zuul过滤器 设置请求头参数
    public  String addToken(String userId){
        String token = UUID.randomUUID().toString();
        valueOperations.set(token,userId, TIME, TimeUnit.MINUTES);
        return token;
    }
    //删除
    public void deleteToken(HttpServletRequest request){
        String token = request.getHeader(NAME);
        if (token!=null){
            redisTemplate.delete(token);
        }
    }
    //解析
    public String parseToken(HttpServletRequest request){
        String token = request.getHeader(NAME);
        return token == null ? null : (String)this.valueOperations.get(token);
    }

    //刷新
    public boolean refreshToken(HttpServletRequest request){
        String token = request.getHeader(NAME);
        return token == null ? false : this.redisTemplate.expire(token, TIME, TimeUnit.MINUTES);
    }
    //解析 并 刷新
    public String parseRrefreshToken(HttpServletRequest request){
        String token = request.getHeader(NAME);
        if (null!=token && redisTemplate.expire(token, TIME, TimeUnit.MINUTES))
            return (String)this.valueOperations.get(token);
        else return null;
    }
}
