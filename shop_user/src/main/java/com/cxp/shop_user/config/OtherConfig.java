package com.cxp.shop_user.config;

import com.cxp.shop_api.util.TokenUtil;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OtherConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    @Bean
    public TokenUtil tokenUtil(RedisTemplate redisTemplate, ValueOperations valueOperations){
        return new TokenUtil(redisTemplate,valueOperations);
    }

}
