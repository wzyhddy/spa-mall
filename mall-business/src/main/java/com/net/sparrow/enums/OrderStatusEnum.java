package com.net.sparrow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 */
@AllArgsConstructor
@Getter
public enum OrderStatusEnum {

    /**
     * 下单
     */
    CREATE(1, "下单"),

    /**
     * 支付
     */
    PAY(2, "支付"),

    /**
     * 完成
     */
    FINISH(3, "完成"),

    /**
     * 取消
     */
    CANCEL(4, "取消");

    private Integer value;

    private String desc;
}
