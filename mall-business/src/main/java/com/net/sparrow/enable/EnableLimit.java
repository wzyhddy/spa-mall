package com.net.sparrow.enable;

import com.net.sparrow.config.LimitConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Sparrow
 * @Description: EnableLimit
 * @DateTime: 2025/2/22 13:22
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LimitConfig.class})
public @interface EnableLimit {
}
