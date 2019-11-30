package com.cxp.shop_favorite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.cxp.shop_favorite.mapper")
@SpringBootApplication
public class ShopFavoriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopFavoriteApplication.class, args);
    }

}
