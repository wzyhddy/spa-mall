package com.net.sparrow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 七牛云配置
 */
@Component
@ConfigurationProperties(prefix = "oss.qiniu")
@Data
public class QiNiuConfig {

    /**
     * AccessKey
     */
    private String accessKey;
    /**
     * SecretKey
     */
    private String secretKey;
    /**
     * 图片存储空间名
     */
    private String bucketPictureName;
    /**
     * 图片外链
     */
    private String domainPicture;
    /**
     * 文件存储空间名
     */
    private String bucketFileName;
    /**
     * 文件外链
     */
    private String domainFile;
}