package com.cxp.shop_zuul.config;

import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.util.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
public class OtherConfig {

    @Bean
    public TokenUtil tokenUtil(RedisTemplate redisTemplate, ValueOperations valueOperations){
        return new TokenUtil(redisTemplate,valueOperations);
    }


    @Bean       //这里是字符串 方便传
    public String USER_ID_LOGIN_OVERDUE(ObjectMapper objectMapper){
        String msg =null;
        try {
            msg =  objectMapper.writeValueAsString(ResultFactory.createFailResult(ResultStatus.USER_ID_LOGIN_OVERDUE));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
