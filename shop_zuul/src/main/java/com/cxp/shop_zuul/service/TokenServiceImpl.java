package com.cxp.shop_zuul.service;

import com.cxp.shop_zuul.exception.StoreNotRegisterException;
import com.cxp.shop_zuul.exception.UserIdLoginOverdueException;
import com.cxp.shop_zuul.service.TokenService;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 直接通过添加字段来 刷新过期时间
 */

@Service
public class TokenServiceImpl extends TokenService {

    static final String KEY_REFRESH_TIME = "refreshTime";

    @Override
    public  String getUserLoginToken(Integer userId){
        HashMap<String, Object> map = new HashMap<>();
        map.put(KEY_USER_ID, userId);
        return createJWT(map);
    }


    public String parseUserId(RequestContext ctx) throws UserIdLoginOverdueException {
        String token = ctx.getRequest().getHeader(KEY_NAME);
        if (token == null)
            throw USER_ID_LOGIN_OVERDUE_EXCEPTION;
        Map<String, Object> map = verifyJwt(token);
        //如果 过了刷新时间就 重新生成token
        if (System.currentTimeMillis() > (Long) map.get(KEY_REFRESH_TIME)){
            ctx.addZuulResponseHeader(KEY_TOKEN, createJWT(map));
        }
        return map.get(KEY_USER_ID).toString();
    }

    @Override
    public String parseStoreId(RequestContext ctx) throws UserIdLoginOverdueException, StoreNotRegisterException {

        String token = ctx.getRequest().getHeader(KEY_NAME);
        if (token == null)
            throw USER_ID_LOGIN_OVERDUE_EXCEPTION;
        Map<String, Object> map = verifyJwt(token);


        if (map.get(KEY_STORE_ID) == null){
            Integer storeId = storeFeignClient.selStoreIdByUserId((Integer) map.get(KEY_USER_ID));
            if (null != storeId){
//                System.out.println("第一次 找不到去店铺微服务找 找到了");
                map.put(KEY_STORE_ID, storeId);
                ctx.addZuulResponseHeader(KEY_TOKEN, createJWT(map));
                return storeId.toString();
            }else
                throw STORE_NOT_REGISTER_EXCEPTION;
        }else {
            //                System.out.println("第二次 直接找到storeId了");
            if (System.currentTimeMillis() > (Long) map.get(KEY_REFRESH_TIME)){
//                System.out.println("刷新token");
                ctx.addZuulResponseHeader(KEY_TOKEN, createJWT(map));
            }
            return map.get(KEY_STORE_ID).toString();
        }
    }





    /**
     * jwt 加密解密密钥
     */
    private static final String JWT_SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

    public static final String jwtId = "tokenId";
    /**
     * 创建JWT
     */
    public  String createJWT(Map<String, Object> map) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
//        Date now = new Date(System.currentTimeMillis());
        map.put(KEY_REFRESH_TIME, System.currentTimeMillis() + TOKEN_REFRESH_TIME);
        Date exp = new Date(System.currentTimeMillis() + TOKEN_EXPIRED_TIME);

        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(map)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
//                .setId(jwtId)                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
//                .setIssuedAt(now)           //iat: jwt的签发时间
                .signWith(signatureAlgorithm, JWT_SECRET)//设置签名使用的签名算法和签名使用的秘钥
                .setExpiration(exp);     //设置过期时间
        return builder.compact();
    }

    /**
     * 验证jwt
     */
    public static Map<String, Object> verifyJwt(String token) throws UserIdLoginOverdueException {
        //签名秘钥，和生成的签名的秘钥一模一样
        try {
            return  (Map<String, Object>) Jwts.parser()  //得到DefaultJwtParser
                    .setSigningKey(JWT_SECRET)         //设置签名的秘钥
                    //.parseClaimsJws(token).getBody();
                    .parse(token).getBody();

        } catch (Exception e) {
            throw USER_ID_LOGIN_OVERDUE_EXCEPTION;
        }//设置需要解析的jwt

    }
}