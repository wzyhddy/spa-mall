package com.net.sparrow.annotation;

import com.net.sparrow.enums.ExcelBizTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel数据导出注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {

    /**
     * 业务类型
     *
     * @return 业务类型
     */
    ExcelBizTypeEnum value();
}
