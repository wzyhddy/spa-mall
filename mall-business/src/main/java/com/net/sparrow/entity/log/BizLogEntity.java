package com.net.sparrow.entity.log;

import com.net.sparrow.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务日志实体 该项目是知识星球：java突击队 的内部项目
 */
@ApiModel("业务日志实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BizLogEntity extends BaseEntity {


	/**
	 * 方法名称
	 */
	@ApiModelProperty("方法名称")
	private String methodName;

	/**
	 * 描述
	 */
	@ApiModelProperty("描述")
	private String description;

	/**
	 * 请求ip
	 */
	@ApiModelProperty("请求ip")
	private String requestIp;

	/**
	 * 浏览器
	 */
	@ApiModelProperty("浏览器")
	private String browser;

	/**
	 * 请求地址
	 */
	@ApiModelProperty("请求地址")
	private String url;

	/**
	 * 请求参数
	 */
	@ApiModelProperty("请求参数")
	private String param;

	/**
	 * 耗时
	 */
	@ApiModelProperty("耗时")
	private Integer time;

	/**
	 * 异常
	 */
	@ApiModelProperty("异常")
	private String exception;

	/**
	 * 状态 1:成功 0:失败
	 */
	@ApiModelProperty("状态 1:成功 0:失败")
	private Integer status;
}
