package org.dows.aac.security.utils;

//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTCreator;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.Claim;
//import com.auth0.jwt.interfaces.DecodedJWT;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTUtil {
    /**
     * 传入payload信息获取token
     *
     * @param map payload @param secret
     * @return token
     */
    public static String getToken(Map<String, String> map, String secret) {
        JwtBuilder builder= Jwts.builder();
        //1、创建加密的技术-sha-256
        io.jsonwebtoken.SignatureAlgorithm signatureAlgorithm= io.jsonwebtoken.SignatureAlgorithm.HS256;
        //2、创建JWT建造者对象
        JwtBuilder jwtBuilder= Jwts.builder();
        //追加token时间
        Calendar instance = Calendar.getInstance();
        //默认7天过期
        instance.add(Calendar.DATE, 7);
        jwtBuilder.setExpiration(instance.getTime());
//        map.put("startTime", String.valueOf(System.currentTimeMillis()));
//        map.put("endTime", String.valueOf(instance.getTimeInMillis()));
        //3、设置JWt相关信息
        jwtBuilder.setIssuedAt(new Date());//开始时间
        jwtBuilder.setSubject(JSON.toJSONString(map));//设置JWT中的内容
        jwtBuilder.signWith(signatureAlgorithm, createKey(secret));
        //4、生成JWT
        return jwtBuilder.compact();

//        JWTCreator.Builder builder = JWT.create();
//        //payload
//        map.forEach(builder::withClaim);
//        Calendar instance = Calendar.getInstance();
//        //默认7天过期
//        instance.add(Calendar.DATE, 7);
//        //指定令牌的过期时间
//        builder.withExpiresAt(instance.getTime());
//        return builder.sign(Algorithm.HMAC256(secret));
    }

    //解析 令牌
    public static String parseJWT(String token, String secret){
        Claims claims=Jwts.parser().setSigningKey(createKey(secret)).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    //校验 令牌
    public static boolean verify(String token, String secret){
        Claims claims=Jwts.parser().setSigningKey(createKey(secret)).parseClaimsJws(token).getBody();

        Date expirationDate = claims.getExpiration();

        // 检查是否过期
        return expirationDate.before(new Date());
    }

    //生成秘钥
    private static SecretKey createKey(String secret){
        byte[] keys=secret.getBytes();
        SecretKey key=new SecretKeySpec(keys,0,keys.length,"AES");
        return key;
    }


//    /**
//     * 验证token 合法性
//     */
//    public static DecodedJWT verify(String token, String secret) {
//        //如果有任何验证异常，此处都会抛出异常
//        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
//    }
//
//    /**
//     * 获取token信息方法
//     */
//    public static Map<String, Claim> getTokenInfo(String token, String secret) {
//        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getClaims();
//    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "1222");
        map.put("accountId", "1222");
        String token = JWTUtil.getToken(map, "1111111");
        System.out.println(token);

        String obj = JWTUtil.parseJWT(token, "1111111");
        System.out.println(obj);

        boolean check = JWTUtil.verify(token, "1111111");
        System.out.println(check);
    }
}