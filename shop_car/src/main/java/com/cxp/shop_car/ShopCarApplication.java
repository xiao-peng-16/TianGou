package com.cxp.shop_car;

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
@MapperScan("com.cxp.shop_car.mapper")
@SpringBootApplication
public class ShopCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopCarApplication.class, args);
    }

}
