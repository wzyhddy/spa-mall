package com.net.sparrow.config.properties;

import lombok.Data;

/**
 * 支付宝支付配置
 */
@Data
public class AliPayProperties {

    private String protocol;
    private String gatewayHost;
    private String signType;
    private String appId;
    private String privateKey;
    private String publicKey;
    private String notifyUrl;
}