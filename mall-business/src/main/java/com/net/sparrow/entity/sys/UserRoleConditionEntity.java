package com.net.sparrow.entity.sys;

import com.net.sparrow.entity.RequestPageEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 用户角色关联查询条件实体
 * 
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@ApiModel("用户角色关联查询条件实体")
@Data
public class UserRoleConditionEntity extends RequestPageEntity {
	

	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  用户ID
     */
	@ApiModelProperty("用户ID")
	private Long userId;

	/**
	 *  角色ID
     */
	@ApiModelProperty("角色ID")
	private Long roleId;

	/**
	 * 用户ID集合
	 */
	@ApiModelProperty("用户ID集合")
	private List<Long> userIdList;

}
