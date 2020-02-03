package com.cxp.shop_zuul.filter;

import com.cxp.shop_zuul.service.RedisTokenServiceImpl;
import com.cxp.shop_zuul.service.TokenService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
*       提高redis模拟session
*       从请求体获取 Token （key） 通过redis获取 userId（values） 放入请求头参数
*       并刷新 Token在redis的存活时间
* */

@Component
public class TokenFilter extends ZuulFilter {

    @Resource
    TokenService tokenService;



    // run（）中 ctx.setSendZuulResponse(false); 经常导致 获取不了请求头信息，使用逻辑写这里了
    @Override
    public boolean shouldFilter() {   //是否使用该过滤器
        RequestContext ctx = RequestContext.getCurrentContext();//获取当前的 上下文对象
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Headers","token, content-type");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Expose-Headers","X-forwared-port, X-forwarded-host, token");
        response.setHeader("Vary","Origin,Access-Control-Request-Method,Access-Control-Request-Headers");


        return tokenService.shouldFilter(ctx);
    }


    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();//获取当前的 上下文对象
        ctx.setSendZuulResponse(false);
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }   //数字越小优先级越高


}
