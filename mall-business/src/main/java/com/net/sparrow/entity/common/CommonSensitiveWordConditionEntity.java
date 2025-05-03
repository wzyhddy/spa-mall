package com.net.sparrow.entity.common;

import com.net.sparrow.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 敏感词查询条件实体
 */
@ApiModel("敏感词查询条件实体")
@Data
public class CommonSensitiveWordConditionEntity extends RequestPageEntity {


	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  类型 1:广告 2:政治 3：违法 4：色情 5：网址
     */
	@ApiModelProperty("类型 1:广告 2:政治 3：违法 4：色情 5：网址")
	private Integer type;

	/**
	 *  名称
     */
	@ApiModelProperty("名称")
	private String word;

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
