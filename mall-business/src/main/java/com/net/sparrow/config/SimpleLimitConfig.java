package com.net.sparrow.config;

import com.net.sparrow.interceptor.SimpleLimitAspect;
import org.springframework.context.annotation.Bean;

/**
 * 简单限流功能配置
 */
public class SimpleLimitConfig {

    @Bean
    public SimpleLimitAspect simpleLimitAspect() {
        return new SimpleLimitAspect();
    }
}