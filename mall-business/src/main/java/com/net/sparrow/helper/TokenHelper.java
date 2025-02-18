package com.net.sparrow.helper;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.net.sparrow.entity.BaseEntity;
import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @Author: Sparrow
 * @Description: tokenHelper
 * @DateTime: 2025/2/17 23:01
 **/
@Slf4j
@Component
public class TokenHelper {

	private static final String TOKEN_PREFIX = "token:";

	private static final String USER_PREFIX = "user:";

	@Value("${mall.mgt.tokenSecret:123456test}")
	private String tokenSecret;

	@Value("${mall.mgt.tokenExpireTimeInRecord:3600}")
	private int tokenExpireTimeInRecord;

	@Autowired
	private RedisUtil redisUtil;


	/**
	 * 生成token
	 * @param userDetails
	 * @return
	 */
	public String generateToken(UserDetails userDetails) {
		String username = userDetails.getUsername();
		//调用Jwt类创建token
		String token = Jwts.builder().setSubject(username).setExpiration(generateExpired())
				.signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
		redisUtil.set(getTokenKey(username), token, tokenExpireTimeInRecord);
		String userStr = JSON.toJSONString(userDetails);
		redisUtil.set(getUserKey(username), userStr, tokenExpireTimeInRecord);
		return token;
	}

	private String getKey(String prefix, String userName) {
		return String.format("%s%s", prefix, userName);
	}

	private String getTokenKey(String username) {
		return getKey(TOKEN_PREFIX, username);
	}

	private String getUserKey(String username) {
		return getKey(USER_PREFIX, username);
	}

	/**
	 * 获取token
	 * @param username 用户名称
	 * @return token
	 */
	public String getToken(String username) {
		return redisUtil.get(getKey(TOKEN_PREFIX, username));
	}

	/**
	 * 删除token
	 */
	public void delToken(String token) {
		String username = getUsernameFromToken(token);
		redisUtil.del(getKey(TOKEN_PREFIX, username));
		redisUtil.del(getKey(USER_PREFIX, username));
	}

	/**
	 * 获取用户详情
	 *
	 * @param username 用户名称
	 * @return 用户详情
	 */
	public UserDetails getUserDetails(String username) {
		String userJson = redisUtil.get(getKey(USER_PREFIX, username));
		return JSONUtil.toBean(userJson, UserDetails.class);
	}

	private Date generateExpired() {
		return new Date(System.currentTimeMillis() + tokenExpireTimeInRecord * 1000);
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
	 * 根据用户名称查询用户详情信息
	 *
	 * @param username 用户名称
	 * @return 用户详情
	 */
	public UserDetails getUserDetailsFromUsername(String username) {
		String userKey = getUserKey(username);
		String userDetailJson = redisUtil.get(userKey);
		if (!StringUtils.hasLength(userDetailJson)) {
			return null;
		}
		return JSON.parseObject(userDetailJson, JwtUserEntity.class);
	}

	/**
	 * 获得 Claims
	 * @param token Token
	 * @return Claims
	 */
	public Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			log.warn("获得 Claims失败：", e);
			claims = null;
		}
		return claims;
	}


	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AssertUtil.notNull(authentication, "当前登录状态过期");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userDetails.getUsername();
	}

	public void fillUpdateUserInfo(BaseEntity baseEntity) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AssertUtil.notNull(authentication, "当前登录状态过期");
		JwtUserEntity jwtUserEntity = (JwtUserEntity) authentication.getPrincipal();
		baseEntity.setUpdateUserId(jwtUserEntity.getId());
		baseEntity.setUpdateUserName(jwtUserEntity.getUsername());
	}
}
