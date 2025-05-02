package com.net.sparrow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件类型
 */
@Getter
@AllArgsConstructor
public enum EmailTypeEnum {

    REMOTE_LOGIN(1, "异地登录提醒");

    /**
     * 枚举值
     */
    private Integer value;


    /**
     * 枚举描述
     */
    private String desc;
}
