
package com.net.sparrow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExcelBizTypeEnum {

	MENU(1, "com.net.sparrow.entity.sys.MenuConditionEntity", "菜单"),
	ROLE(2, "com.net.sparrow.entity.sys.RoleConditionEntity", "角色"),
	DEPT(3, "com.net.sparrow.entity.sys.DeptConditionEntity", "部门"),
	USER(4, "com.net.sparrow.entity.sys.UserConditionEntity", "用户"),
	JOB(5, "com.net.sparrow.entity.sys.JobConditionEntity", "岗位");

	/**
	 * 枚举值
	 */
	private Integer value;

	/**
	 * 请求参数实体
	 */
	private String requestEntity;

	/**
	 * 枚举描述
	 */
	private String desc;
}