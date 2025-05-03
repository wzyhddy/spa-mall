package com.net.sparrow.entity.common;

import com.net.sparrow.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 定时任务执行日志实体
 */
@ApiModel("定时任务执行日志实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonJobLogEntity extends BaseEntity {


	/**
	 * 定时任务ID
	 */
	@ApiModelProperty("定时任务ID")
	private Long jobId;

	/**
	 * 定时任务名称
	 */
	@ApiModelProperty("定时任务名称")
	private String jobName;

	/**
	 * 执行状态 1：执行中 2：暂停 3：成功 4：失败
	 */
	@ApiModelProperty("执行状态 1：执行中 2：暂停 3：成功 4：失败")
	private Integer runStatus;

	/**
	 * bean名称
	 */
	@ApiModelProperty("bean名称")
	private String beanName;

	/**
	 * 方法名称
	 */
	@ApiModelProperty("方法名称")
	private String methodName;

	/**
	 * cron 表达式
	 */
	@ApiModelProperty("cron 表达式")
	private String cronExpression;

	/**
	 * 参数
	 */
	@ApiModelProperty("参数")
	private String params;

	/**
	 * 开始执行时间
	 */
	@ApiModelProperty("开始执行时间")
	private Date startTime;

	/**
	 * 执行结束时间
	 */
	@ApiModelProperty("执行结束时间")
	private Date endTime;

	/**
	 * 异常信息
	 */
	@ApiModelProperty("异常信息")
	private String exception;
}
