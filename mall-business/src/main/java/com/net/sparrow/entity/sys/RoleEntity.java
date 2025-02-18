package com.net.sparrow.entity.sys;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色实体
 * 
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@ApiModel("角色实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleEntity extends BaseEntity {
	

	/**
	 * 名称
	 */
	@ApiModelProperty("名称")
	private String name;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;

	/**
	 * 数据权限
	 */
	@ApiModelProperty("数据权限")
	private String dataScope;

	/**
	 * 角色级别
	 */
	@ApiModelProperty("角色级别")
	private Integer level;

	/**
	 * 功能权限
	 */
	@ApiModelProperty("功能权限")
	private String permission;
}
