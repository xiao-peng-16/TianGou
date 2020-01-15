package com.cxp.shop_zuul.controller;


import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_zuul.service.TokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(allowCredentials = "true")
@RestController
public class TokenController {

    @Autowired
    TokenServiceImpl tokenService;

    @RequestMapping("/getUserLoginToken")
    public String getUserLoginToken(Integer userId){
        return tokenService.getUserLoginToken(userId);
    }


    //退出登录
    @RequestMapping("/outLoginByToken")
    public ResultBean outLoginByToken(HttpServletRequest request){
        return tokenService.deleteToken(request);
    }



}
