package com.net.sparrow.interceptor;

import com.net.sparrow.annotation.Limit;
import com.net.sparrow.entity.auth.JwtUserEntity;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.util.IpUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 基于Redis的限流功能
 *
 */
@Slf4j
@Aspect
public class LimitAspect {

    private static final String LIMIT_ERROR_MESSAGE = "系统繁忙,请稍后再试";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DefaultRedisScript redisScript;

    @Around("@annotation(com.net.sparrow.annotation.Limit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //拿limit的注解
        Limit limit = method.getAnnotation(Limit.class);
        if (limit != null) {
            String limitKey = buildLimitKey(limit, method);
            int count = limit.permitsPerSecond();
            long time = limit.timeOut();
            Object number = stringRedisTemplate.execute(redisScript, Lists.newArrayList(limitKey), count, time);
            if (Objects.nonNull(number) && number instanceof Long) {
                if (((Long) number).longValue() > count) {
                    throw new BusinessException(LIMIT_ERROR_MESSAGE);
                }
            }
        }
        return joinPoint.proceed();
    }


    private String buildLimitKey(Limit limit, Method method) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        switch (limit.limitType()) {
            case IP:
                String ipAddr = IpUtil.getIpAddr(httpServletRequest);
                return String.format("limitRate:ip:%s_%s", method.getName(), ipAddr);
            case USER_ID:
                JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
                if (Objects.nonNull(currentUserInfo)) {
                    throw new BusinessException("请先登录");
                }
                return String.format("limitRate:userId:%s_%s", method.getName(), currentUserInfo.getId());
            default:
                return String.format("limitRate:all:%s", method.getName());
        }
    }
}
