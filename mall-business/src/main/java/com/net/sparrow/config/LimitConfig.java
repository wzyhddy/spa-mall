package com.net.sparrow.config;

import com.net.sparrow.interceptor.LimitAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @Author: Sparrow
 * @Description: 分布式环境中限流功能配置
 * @DateTime: 2025/2/22 13:25
 **/
public class LimitConfig {

	@Bean
	public LimitAspect limitAspect() {
		return new LimitAspect();
	}

	@Bean
	public DefaultRedisScript<Long> limitScript() {
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptText(limitScriptText());
		redisScript.setResultType(Long.class);
		return redisScript;
	}

	/**
	 * 限流lua脚本
	 */
	private String limitScriptText() {
		return "local key = KEYS[1]\n" +
				"local count = tonumber(ARGV[1])\n" +
				"local time = tonumber(ARGV[2])\n" +
				"local current = redis.call('get', key);\n" +
				"if current == nil then\n current = 0\n end\n" +
				"if current and tonumber(current) > count then\n" +
				"    return tonumber(current);\n" +
				"end\n" +
				"current = redis.call('incr', key)\n" +
				"if tonumber(current) == 1 then\n" +
				"    redis.call('expire', key, time)\n" +
				"end\n" +
				"return tonumber(current);";
	}

}
