package com.net.sparrow.entity.sys;

import com.net.sparrow.entity.RequestConditionEntity;
import com.net.sparrow.entity.RequestPageEntity;
import lombok.Data;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 部门查询条件实体
 * 
 * @author sparrow
 * @date 2025-02-17 20:14:34
 */
@ApiModel("部门查询条件实体")
@Data
public class DeptConditionEntity extends RequestConditionEntity {
	

	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  名称
     */
	@ApiModelProperty("名称")
	private String name;

	/**
	 *  上级部门ID
     */
	@ApiModelProperty("上级部门ID")
	private Long pid;

	/**
	 *  有效状态 1:有效 0:无效
     */
	@ApiModelProperty("有效状态 1:有效 0:无效")
	private Integer validStatus;

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

	/**
	 * 是否查询树
	 */
	@ApiModelProperty("是否查询树")
	private Boolean queryTree;

	/**
	 * idList
	 */
	@ApiModelProperty("idList")
	private List<Long> idList;
}
