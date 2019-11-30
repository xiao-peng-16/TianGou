package com.cxp.shop_commodity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.cxp.shop_commodity.mapper")
@SpringBootApplication
public class ShopCommodityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopCommodityApplication.class, args);
    }

}
