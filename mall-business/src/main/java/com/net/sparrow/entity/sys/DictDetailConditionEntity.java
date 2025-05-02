package com.net.sparrow.entity.sys;

import com.net.sparrow.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@ApiModel("数据查询条件详情实体")
@Data
public class DictDetailConditionEntity extends RequestPageEntity {

	/**
	 * 数据字典名称
	 */
	@NotEmpty(message = "数据字典名称不能为空")
	@ApiModelProperty("数据字典名称")
	private String dictName;


	/**
	 * ID
	 */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 * 数据字典id
	 */
	@ApiModelProperty("数据字典id")
	private Long dictId;

	/**
	 * 数据字典id集合
	 */
	@ApiModelProperty("数据字典id集合")
	private List<Long> dictIdList;

	/**
	 * 值
	 */
	@ApiModelProperty("值")
	private String value;

	/**
	 * 文本
	 */
	@ApiModelProperty("文本")
	private String label;

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
