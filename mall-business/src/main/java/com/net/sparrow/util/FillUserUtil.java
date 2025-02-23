package com.net.sparrow.util;


import com.net.sparrow.entity.BaseEntity;
import com.net.sparrow.entity.auth.JwtUserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.function.Supplier;

/**
 * 填充用户工具
 */
public abstract class FillUserUtil {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_NAME = "系统管理员";
    private static final String ANONYMOUS_USER = "anonymousUser"; //匿名用户

    private FillUserUtil() {
    }

    /**
     * 填充创建用户信息
     *
     * @param baseEntity 实体
     */
    public static void fillCreateUserInfo(BaseEntity baseEntity) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AssertUtil.notNull(authentication, "当前登录状态过期");
        if (authentication.getPrincipal() instanceof String) {
            if (ANONYMOUS_USER.equals(authentication.getPrincipal())) {
                baseEntity.setCreateUserId(DEFAULT_USER_ID);
                baseEntity.setCreateUserName(DEFAULT_USER_NAME);
                baseEntity.setCreateTime(new Date());
            }
        } else {
            JwtUserEntity jwtUserEntity = (JwtUserEntity) authentication.getPrincipal();
            baseEntity.setCreateUserId(jwtUserEntity.getId());
            baseEntity.setCreateUserName(jwtUserEntity.getUsername());
            baseEntity.setCreateTime(new Date());
        }
    }

    /**
     * 填充修改用户信息
     *
     * @param baseEntity 实体
     */
    public static void fillUpdateUserInfo(BaseEntity baseEntity) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AssertUtil.notNull(authentication, "当前登录状态过期");
        JwtUserEntity jwtUserEntity = (JwtUserEntity) authentication.getPrincipal();
        baseEntity.setUpdateUserId(jwtUserEntity.getId());
        baseEntity.setUpdateUserName(jwtUserEntity.getUsername());
        baseEntity.setUpdateTime(new Date());
    }

    /**
     * 从实体的创建用户信息中填充修改用户信息
     *
     * @param baseEntity 实体
     */
    public static void fillUpdateUserInfoFromCreate(BaseEntity baseEntity) {
        baseEntity.setUpdateUserId(baseEntity.getCreateUserId());
        baseEntity.setUpdateUserName(baseEntity.getCreateUserName());
        baseEntity.setUpdateTime(new Date());
    }

    public static JwtUserEntity getCurrentUserInfo() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AssertUtil.notNull(authentication, "当前登录状态过期");
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return null;
        }
        return (JwtUserEntity) authentication.getPrincipal();
    }

    /**
     * 填充默认用户信息
     * @param baseEntity 实体
     */
    public static void fillUpdateDefaultUserInfo(BaseEntity baseEntity) {
        baseEntity.setUpdateUserId(DEFAULT_USER_ID);
        baseEntity.setUpdateUserName(DEFAULT_USER_NAME);
        baseEntity.setUpdateTime(new Date());
    }
    /**
     * mock当前登录的用户
     */
    public static void mockCurrentUser() {
        JwtUserEntity jwtUserEntity = new JwtUserEntity();
        jwtUserEntity.setId(DEFAULT_USER_ID);
        jwtUserEntity.setUsername(DEFAULT_USER_NAME);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUserEntity, null, jwtUserEntity.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 清空当前登录用户信息
     */
    public static void clearCurrentUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    /**
     * mock当前登录的用户
     *
     * @param supplier 业务逻辑
     * @param <T>      泛型
     * @return 返回结果
     */
    public static <T> T mockCurrentUser(Supplier<T> supplier) {
        mockCurrentUser();
        try {
            return supplier.get();
        } finally {
            clearCurrentUser();
        }
    }
}
