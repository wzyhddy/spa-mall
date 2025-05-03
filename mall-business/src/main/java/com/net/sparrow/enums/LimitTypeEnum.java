package com.net.sparrow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限流类型
 */
@Getter
@AllArgsConstructor
public enum LimitTypeEnum {

    /**
     * 针对访问接口的所有请求
     */
    ALL(0, "所有"),

    /**
     * 针对访问接口的指定IP
     */
    IP(1, "用户ip"),

    /**
     * 针对访问接口的指定用户
     */
    USER_ID(2, "用户ID");


    /**
     * 枚举值
     */
    private Integer value;


    /**
     * 枚举描述
     */
    private String desc;
}
