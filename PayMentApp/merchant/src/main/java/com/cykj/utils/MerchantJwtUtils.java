package com.cykj.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhangzhenhua on 2024/8/8.
 * JSON Web Token 工具类
 */
public class MerchantJwtUtils {

    //定义私钥
    private static String privateKey =
            "test1234test1234test1234test1234test1234test1234test1234test1234test1234test1234";
    private static int expireTime = 1000 * 60 * 60 * 24;

    /**
     * 生成jwt token
     *
     * @param claims 要加密的放在token负载里面的数据
     * @return
     */
    public static String generateToken(Map<String, Object> claims) {
        //创建token
        String token = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();
        System.out.println("生成token：" + token);
        return token;
    }

    /**
     * 解析jwt token
     *
     * @param token
     * @return
     */
    public static Claims parseToken(String token) {
        Claims body = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody();
        return body;
    }

}