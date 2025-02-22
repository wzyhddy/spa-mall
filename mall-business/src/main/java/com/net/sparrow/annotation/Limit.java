package com.net.sparrow.annotation;

import com.net.sparrow.enums.LimitTypeEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Sparrow
 * @Description: 限流注解
 * @DateTime: 2025/2/22 12:44
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit {

	/**
	 * 资源的key，唯一
	 * 作用：不同的接口，不同的流量控制
	 * @return
	 */
	String key() default "";

	/**
	 * 最多的访问限制次数
	 */
	int permitsPerSecond();

	/**
	 * 获取令牌最大等待时间
	 * @return
	 */
	long timeOut();

	/**
	 * 获取令牌最大等待时间，单位(例:分钟/秒/毫秒) 默认:毫秒
	 * @return
	 */
	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
	/**
	 * 限流类型，默认是整个接口
	 */
	LimitTypeEnum limitType() default LimitTypeEnum.ALL;


}
