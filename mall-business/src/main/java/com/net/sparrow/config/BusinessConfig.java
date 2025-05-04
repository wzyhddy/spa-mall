package com.net.sparrow.config;

import com.net.sparrow.config.properties.AliPayProperties;
import com.net.sparrow.config.properties.QuartzThreadPoolProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 业务配置类
 */
@Data
@Component
@Slf4j
@ConfigurationProperties(prefix = "mall.mgt")
public class BusinessConfig {

    private QuartzThreadPoolProperties QuartzThreadPoolConfig = new QuartzThreadPoolProperties();

    /**
     * 商品搜索index名称
     */
    private String productEsIndexName = "product-es-index-v1";

    /**
     * 支付宝支付相关配置
     */
    private AliPayProperties aliPayConfig = new AliPayProperties();
}