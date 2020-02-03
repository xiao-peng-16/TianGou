package com.cxp.shop_zuul.config;

import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_zuul.service.RedisTokenServiceImpl;
import com.cxp.shop_zuul.service.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtherConfig {


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


    @Bean       //这里是字符串 方便传
    public String STORE_NOT_REGISTER(ObjectMapper objectMapper){
        String msg =null;
        try {
            msg =  objectMapper.writeValueAsString(ResultFactory.createFailResult(ResultStatus.STORE_NOT_REGISTER));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
