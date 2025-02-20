package com.net.sparrow.annotation;

import com.net.sparrow.enums.TaskTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncTask {

    /**
     * 任务类型
     *
     * @return
     */
    TaskTypeEnum value();
}