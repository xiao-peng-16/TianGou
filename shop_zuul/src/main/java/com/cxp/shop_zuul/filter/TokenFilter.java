package com.cxp.shop_zuul.filter;

import com.cxp.shop_api.util.TokenUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*       提高redis模拟session
*       从请求体获取 Token （key） 通过redis获取 userId（values） 放入请求头参数
*       并刷新 Token在redis的存活时间
* */

@Component
public class TokenFilter extends ZuulFilter {

    @Autowired
    TokenUtil tokenUtil;
    @Resource
    String USER_ID_LOGIN_OVERDUE;
    static final String TARGET_URI = "ByUserId";

    // run（）中 ctx.setSendZuulResponse(false); 经常导致 获取不了请求头信息，使用逻辑写这里了
    @Override
    public boolean shouldFilter() {   //是否使用该过滤器
        RequestContext ctx = RequestContext.getCurrentContext();//获取当前的 上下文对象
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

//        response.setHeader("Access-Control-Allow-Origin","http://localhost:8000");
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Headers","token, content-type");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Expose-Headers","X-forwared-port, X-forwarded-host");
        response.setHeader("Vary","Origin,Access-Control-Request-Method,Access-Control-Request-Headers");

        String requestURI = request.getRequestURI();   //获取请求的uri
        if (requestURI.endsWith(TARGET_URI)){
            String userId = tokenUtil.parseRrefreshToken(request);  //解析并刷新出userId
            System.out.println("token    => "+request.getHeader("token"));
            System.out.println("userId   => "+userId);
            if (userId == null ){
                return true;
            }else {
//                request.getParameterMap();    //必须加这句 不然ctx.getRequestQueryParams()失败
//                Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();//参数键值对map
                //如果一开始请求头里没参数 那么requestQueryParams为空
//                if (requestQueryParams==null) requestQueryParams=new HashMap<>();

                //为了提高性能 ，其他参数都放请求体了，用post请求
                Map<String, List<String>> requestQueryParams=new HashMap<>();
                List<String> arrayList = new ArrayList<>();
                arrayList.add(userId);      //添加 对应的values
                requestQueryParams.put("userId", arrayList); //把参数键值对放入map
                ctx.setRequestQueryParams(requestQueryParams);  //重新设置请求头所含有的参数
            }
        }
        return false;
    }


    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();//获取当前的 上下文对象
        ctx.setResponseBody(USER_ID_LOGIN_OVERDUE);
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
