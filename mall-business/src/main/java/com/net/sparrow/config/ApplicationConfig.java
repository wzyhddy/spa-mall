package com.net.sparrow.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = "com.net.sparrow.mapper")
@Configuration
public class ApplicationConfig {
}