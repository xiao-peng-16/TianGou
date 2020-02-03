package com.cxp.shop_zuul.config;


import com.cxp.shop_zuul.service.RedisTokenServiceImpl;
import com.cxp.shop_zuul.service.TokenService;
import com.cxp.shop_zuul.service.TokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenServiceConfig {

    @Bean
    public TokenService tokenService(){
        //通过redis来 刷新过期时间
//        return new RedisTokenServiceImpl();

        //直接通过添加字段来 刷新过期时间
        return new TokenServiceImpl();
    }

}
