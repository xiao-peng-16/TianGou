package com.cxp.shop_images.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QiniuConfig {

    @Value("${accessKey}")
    String accessKey;

    @Value("${secretKey}")
    String secretKey;


    //身份验证
    @Bean
    public Auth getAuth(){
        return Auth.create(accessKey, secretKey);
    }

    @Bean
    public BucketManager getBucketManager(Auth auth){
        return new BucketManager(auth);
    }

}
