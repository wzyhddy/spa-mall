package com.net.sparrow.config;

import com.net.sparrow.util.SnowFlakeIdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式ID配置
 */
@Configuration
public class IdGenerateConfig {

    @Value("${mall.mgt.id.generate.workerId:1}")
    private int workerId;
    @Value("${mall.mgt.id.generate.dataCenterId:31}")
    private int dataCenterId;

    @Bean
    public SnowFlakeIdWorker snowFlakeIdWorker() {
        return new SnowFlakeIdWorker(workerId, dataCenterId);
    }
}