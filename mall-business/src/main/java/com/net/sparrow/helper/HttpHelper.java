package com.net.sparrow.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 发送http请求工具类
 */
@Slf4j
@Component
public class HttpHelper {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 发送get请求
     *
     * @param url    url地址
     * @param tClass 返回值实体
     * @param <T>    泛型
     * @return 返回值实体
     */
    public <T> T doGet(String url, Class<T> tClass) {
        return restTemplate.getForObject(url, tClass);
    }
}
