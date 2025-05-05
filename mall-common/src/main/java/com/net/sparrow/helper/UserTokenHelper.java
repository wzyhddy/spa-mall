package com.net.sparrow.helper;

import com.net.sparrow.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class UserTokenHelper {

    private static final String TOKEN_PREFIX = "token:";
    private static final String USER_PREFIX = "user:";

    @Getter
    @Value("${mall.mgt.tokenSecret:123456test}")
    private String tokenSecret;
    @Value("${mall.mgt.tokenExpireTimeInRecord:3600}")
    private int tokenExpireTimeInRecord;

    @Autowired
    protected RedisUtil redisUtil;

    /**
     * 生成token
     * @param username 用户名
     * @param json 用户信息
     * @return
     */
    public String generateToken(String username, String json) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(generateExpired())
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        redisUtil.set(getTokenKey(username), token, tokenExpireTimeInRecord);
        redisUtil.set(getUserKey(username), json, tokenExpireTimeInRecord);
        return token;
    }


    public String getTokenKey(String username) {
        return getKey(TOKEN_PREFIX, username);
    }

    public String getUserKey(String username) {
        return getKey(USER_PREFIX, username);
    }


    /**
     * 从token中解析出username
     *
     * @param token token
     * @return username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (Objects.isNull(claims)) {
            return null;
        }
        return claims.getSubject();
    }

    /**
     * 获得 Claims
     *
     * @param token Token
     * @return Claims
     */

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("获得 Claims失败：", e);
            claims = null;
        }
        return claims;
    }


    /**
     * 计算过期时间
     *
     * @return Date
     */
    protected Date generateExpired() {
        return new Date(System.currentTimeMillis() + tokenExpireTimeInRecord * 1000);
    }


    protected String getKey(String prefix, String userName) {
        return String.format("%s%s", prefix, userName);
    }
}
