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
 * JWT 令牌的工具类，用来生成和解析 JWT 令牌
 */
@Slf4j
public class JwtUtils {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 令牌过期时间：12小时（单位：毫秒）
    private static final long EXPIRATION = 12 * 60 * 60 * 1000L; // 12小时

    /**
     * 生成 JWT 令牌
     *
     * @param claims 自定义载荷数据（如 id, username 等）
     * @return 生成的 JWT 字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .compact();
    }

    /**
     * 解析 JWT 令牌
     *
     * @param jwt 要解析的 JWT 字符串
     * @return 解析后的载荷（claims）
     * @throws Exception 当 JWT 无效、过期或签名错误时抛出异常
     */
    public static Claims parseJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}