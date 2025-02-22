package com.net.sparrow;

import com.net.sparrow.enable.EnableLimit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @Author: Sparrow
 * @DateTime: 2025/2/17 17:30
 **/
@EnableCaching
@EnableLimit
@SpringBootApplication(scanBasePackages = "com.net.sparrow")
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
