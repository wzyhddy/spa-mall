package com.net.sparrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Sparrow
 * @DateTime: 2025/2/18 22:20
 **/
@EnableScheduling //开启Spring的定时任务功能
@SpringBootApplication(scanBasePackages = {"com.net.sparrow"})
public class JobApplication {
	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}
}
