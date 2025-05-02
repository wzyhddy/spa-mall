package com.net.sparrow.entity.log;

import com.net.sparrow.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 业务日志查询条件实体
 */
@ApiModel("业务日志查询条件实体")
@Data
public class BizLogConditionEntity extends RequestPageEntity {


	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  方法名称
     */
	@ApiModelProperty("方法名称")
	private String methodName;

	/**
	 *  描述
     */
	@ApiModelProperty("描述")
	private String description;

	/**
	 *  请求ip
     */
	@ApiModelProperty("请求ip")
	private String requestIp;

	/**
	 *  浏览器
     */
	@ApiModelProperty("浏览器")
	private String browser;

	/**
	 *  请求地址
     */
	@ApiModelProperty("请求地址")
	private String url;

	/**
	 *  请求参数
     */
	@ApiModelProperty("请求参数")
	private String param;

	/**
	 *  耗时
     */
	@ApiModelProperty("耗时")
	private Integer time;

	/**
	 *  异常
     */
	@ApiModelProperty("异常")
	private String exception;

	/**
	 *  状态 1:成功 0:失败
     */
	@ApiModelProperty("状态 1:成功 0:失败")
	private Integer status;

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
	 *  是否删除  `is_del` tinyint(1) D除
     */
	@ApiModelProperty("是否删除  `is_del` tinyint(1) D除")
	private Integer isDel;
}
