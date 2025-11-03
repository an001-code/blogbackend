package com.github.an001code.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;


/**
 * jwt令牌的工具类，用来生成和解析jwt令牌
 */

@Slf4j
public class JwtUtils {

    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
       private static Long expire = 43200000L;

       //生成jwt令牌
       public static String generateJwt(Map<String,Object> claims){
           String jwt = Jwts.builder()
                   .setClaims(claims)
                   .signWith(SignatureAlgorithm.HS256,key)
                   .setExpiration(new Date(System.currentTimeMillis()+expire))
                   .compact();
            return jwt;

       }

       //解析jwt令牌
       public static Claims parseJwt(String jwt){
           Claims claims = Jwts.parser()
                   .setSigningKey(key)
                   .parseClaimsJws(jwt)
                   .getBody();
           return claims;
       }
}
