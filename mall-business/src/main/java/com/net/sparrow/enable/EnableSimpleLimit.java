package com.net.sparrow.enable;

import com.net.sparrow.config.SimpleLimitConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Sparrow
 * @Description:
 * @DateTime: 2025/2/22 13:06
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SimpleLimitConfig.class})
public @interface EnableSimpleLimit {

}
