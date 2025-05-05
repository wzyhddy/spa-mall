package com.net.sparrow.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.net.sparrow.entity.auth.AuthUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordUtil {

    @Value("${mall.mgt.password.privateKey:MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9pB6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZUBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3tTbklZkD2A==}")
    private String privateKey;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private PasswordUtil() {
    }


    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 解密RSA密码
     *
     * @param authUserEntity 用户实体
     * @return 密码
     */
    public String decodeRsaPassword(AuthUserEntity authUserEntity) {
        RSA rsa = new RSA(privateKey, null);
        return new String(rsa.decrypt(authUserEntity.getPassword(), KeyType.PrivateKey));
    }
}
