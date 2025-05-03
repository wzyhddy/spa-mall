package com.net.sparrow.entity.common;

import com.net.sparrow.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 定时任务查询条件实体
 */
@ApiModel("定时任务查询条件实体")
@Data
public class CommonJobConditionEntity extends RequestPageEntity {


	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  定时任务名称
     */
	@ApiModelProperty("定时任务名称")
	private String jobName;

	/**
	 *  暂停状态 0：未暂停 1：已暂停
     */
	@ApiModelProperty("暂停状态 0：未暂停 1：已暂停")
	private Integer pauseStatus;

	/**
	 *  bean名称
     */
	@ApiModelProperty("bean名称")
	private String beanName;

	/**
	 *  方法名称
     */
	@ApiModelProperty("方法名称")
	private String methodName;

	/**
	 *  cron 表达式
     */
	@ApiModelProperty("cron 表达式")
	private String cronExpression;

	/**
	 *  参数
     */
	@ApiModelProperty("参数")
	private String params;

	/**
	 *  备注
     */
	@ApiModelProperty("备注")
	private String remark;

	/**
	 *  创建人ID
     */
	@ApiModelProperty("创建人ID")
	private Long createUserId;

	/**
	 *  创建人名称
     */
	@ApiModelProperty("创建人名称")
	private String createUserName;

	/**
	 *  创建日期
     */
	@ApiModelProperty("创建日期")
	private Date createTime;

	/**
	 *  修改人ID
     */
	@ApiModelProperty("修改人ID")
	private Long updateUserId;

	/**
	 *  修改人名称
     */
	@ApiModelProperty("修改人名称")
	private String updateUserName;

	/**
	 *  修改时间
     */
	@ApiModelProperty("修改时间")
	private Date updateTime;

	/**
	 *  是否删除 1：已删除 0：未删除
     */
	@ApiModelProperty("是否删除 1：已删除 0：未删除")
	private Integer isDel;
}
