package com.net.sparrow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 执行状态枚举
 */
@AllArgsConstructor
@Getter
public enum RunStatusEnum {

    /**
     * 执行中
     */
    RUNNING(1, "执行中"),

    /**
     * 暂停
     */
    PAUSE(2, "暂停"),

    /**
     * 成功
     */
    SUCCESS(3, "成功"),

    /**
     * 失败
     */
    FAILURE(4, "失败");

    private Integer value;

    private String desc;
}
