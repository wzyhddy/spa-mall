package com.net.sparrow.quartz;

import com.net.sparrow.config.BusinessConfig;
import com.net.sparrow.config.properties.QuartzThreadPoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务线程池配置类
 */
@Configuration
public class QuartzThreadPoolConfig {

    @Autowired
    private BusinessConfig businessConfig;


    @Bean("quartzThreadPoolExecutor")
    public ThreadPoolExecutor quartzThreadPoolExecutor() {
        QuartzThreadPoolProperties quartzThreadPoolConfig = businessConfig.getQuartzThreadPoolConfig();
        ThreadPoolExecutor threadPoolTaskExecutor = new ThreadPoolExecutor(quartzThreadPoolConfig.getCorePoolSize(),
                quartzThreadPoolConfig.getCorePoolSize(),
                quartzThreadPoolConfig.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(quartzThreadPoolConfig.getQueueSize()),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}