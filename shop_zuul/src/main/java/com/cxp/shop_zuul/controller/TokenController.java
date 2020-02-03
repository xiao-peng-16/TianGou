package com.cxp.shop_zuul.controller;


import com.cxp.shop_zuul.service.TokenService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CrossOrigin(allowCredentials = "true")
@RestController
public class TokenController {

    @Resource
    TokenService tokenService;

    @RequestMapping("/getUserLoginToken")
    public String getUserLoginToken(Integer userId){
        return tokenService.getUserLoginToken(userId);
    }



}
