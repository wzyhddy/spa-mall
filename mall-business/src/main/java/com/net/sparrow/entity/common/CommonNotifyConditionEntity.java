package com.net.sparrow.entity.common;

import com.net.sparrow.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 通知查询条件实体
 */
@ApiModel("通知查询条件实体")
@Data
public class CommonNotifyConditionEntity extends RequestPageEntity {


    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

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

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private Long createUserId;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty("修改人ID")
    private Long updateUserId;

    /**
     * 修改人名称
     */
    @ApiModelProperty("修改人名称")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否删除 1：已删除 0：未删除
     */
    @ApiModelProperty("是否删除 1：已删除 0：未删除")
    private Integer isDel;
}
