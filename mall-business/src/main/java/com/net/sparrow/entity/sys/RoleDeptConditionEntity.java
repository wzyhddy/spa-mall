package com.net.sparrow.entity.sys;

import com.net.sparrow.entity.RequestPageEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色部门关联查询条件实体
 * 
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@ApiModel("角色部门关联查询条件实体")
@Data
public class RoleDeptConditionEntity extends RequestPageEntity {
	

	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  
     */
	@ApiModelProperty("")
	private Long roleId;

	/**
	 *  
     */
	@ApiModelProperty("")
	private Long deptId;
}
