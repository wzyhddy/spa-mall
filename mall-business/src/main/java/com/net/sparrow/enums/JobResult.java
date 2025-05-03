package com.net.sparrow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * job执行结果枚举
 */
@AllArgsConstructor
@Getter
public enum JobResult {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 失败
     */
    FAILURE(1, "失败");

    private Integer value;

    private String desc;
}