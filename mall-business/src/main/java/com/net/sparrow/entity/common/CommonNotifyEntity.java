package com.net.sparrow.entity.common;

import com.net.sparrow.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知实体 该项目是知识星球：java突击队 的内部项目
 */
@ApiModel("通知实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonNotifyEntity extends BaseEntity {


    /**
     * 通知类型 1：excel导出
     */
    @ApiModelProperty("通知类型 1：excel导出")
    private Integer type;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 跳转地址
     */
    @ApiModelProperty("跳转地址")
    private String linkUrl;

    /**
     * 阅读状态 0：未阅读 1：已阅读
     */
    @ApiModelProperty("阅读状态 0：未阅读 1：已阅读")
    private Integer readStatus;

    /**
     * 需要推送通知的用户ID
     */
    @ApiModelProperty("需要推送通知的用户ID")
    private Long toUserId;


    /**
     * 是否已推送
     */
    @ApiModelProperty("是否已推送")
    private Integer isPush;
}
