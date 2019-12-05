package com.cxp.shop_images.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;


@Configuration
public class ImagesUrlPrifixBean {

    @Value("${userPhotoUriPrifix}")
    String userPhotoUriPrifix;

    @Value("${commodityPhotoUriPrifix}")
    String commodityPhotoUriPrifix;

    @Value("${commodityVideoUriPrifix}")
    String commodityVideoUriPrifix;

    public String getHost(){
//        try {
//            String host = InetAddress.getLocalHost().getHostAddress();  //ip
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        return InetAddress.getLoopbackAddress().getHostAddress();//127.0.0.1
    }

    @Bean
    public String getUserPhotoUrlPrifix(){
        return "http://" + getHost() + userPhotoUriPrifix;
    }

    @Bean
    public String getCommodityPhotoUrlPrifix(){
        return "http://" + getHost() + commodityPhotoUriPrifix;
    }

    @Bean
    public String getCommodityVideoUrlPrifix(){
        return "http://" + getHost() + commodityVideoUriPrifix;
    }
}
