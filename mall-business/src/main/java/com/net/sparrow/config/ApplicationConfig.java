package com.net.sparrow.config;

import com.net.sparrow.mybatis.DictCacheKeyGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = "com.net.sparrow.mapper")
@Configuration
public class ApplicationConfig {

	@Bean
	public DictCacheKeyGenerator dictCacheKeyGenerator() {
		return new DictCacheKeyGenerator();
	}

}