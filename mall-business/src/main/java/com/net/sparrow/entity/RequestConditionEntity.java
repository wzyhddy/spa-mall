package com.net.sparrow.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 请求条件实体
 */
@Data
public class RequestConditionEntity extends RequestPageEntity {


    /**
     * 创建日期范围
     */
    @ApiModelProperty("创建日期范围")
    private List<String> betweenTime;

    /**
     * 创建开始时间
     */
    private String createBeginTime;

    /**
     * 创建结束时间
     */
    private String createEndTime;

    /**
     * 自定义excel表头列表
     */
    private List<String> customizeColumnNameList;

    /**
     * 排序字段
     */
    private List<String> sortField;
}
