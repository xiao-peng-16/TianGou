package com.cxp.shop_images;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ShopImagesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopImagesApplication.class, args);
    }

}
