package com.net.sparrow.entity.sys;

import com.alibaba.excel.annotation.ExcelProperty;
import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 菜单实体
 * 
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@ApiModel("菜单实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuEntity extends BaseEntity {

	/**
	 * 菜单名称
	 */
	@ExcelProperty(value = "菜单名称", index = 0)
	@ApiModelProperty("菜单名称")
	private String name;

	/**
	 * 上级菜单ID
	 */
	@ExcelProperty(value = "上级菜单ID", index = 1)
	@ApiModelProperty("上级菜单ID")
	private Long pid;

	/**
	 * 排序
	 */
	@ExcelProperty(value = "排序", index = 2)
	@ApiModelProperty("排序")
	private Integer sort;

	/**
	 * 图标
	 */
	@ExcelProperty(value = "图标", index = 3)
	@ApiModelProperty("图标")
	private String icon;

	/**
	 * 路由
	 */
	@ExcelProperty(value = "路由", index = 4)
	@ApiModelProperty("路由")
	private String path;

	/**
	 * 是否隐藏
	 */
	@ExcelProperty(value = "是否隐藏", index = 5)
	@ApiModelProperty("是否隐藏")
	private Boolean hidden;

	/**
	 * 是否外链 1：是 0：否
	 */
	@ExcelProperty(value = "是否外链", index = 6)
	@ApiModelProperty("是否外链 1：是 0：否")
	private Integer isLink;


	/**
	 * 功能权限
	 */
	@ExcelProperty(value = "功能权限", index = 7)
	@ApiModelProperty("功能权限")
	private String permission;

	/**
	 * 类型 1：目录 2：菜单 3：按钮
	 */
	@ExcelProperty(value = "类型", index = 8)
	@ApiModelProperty("类型 1：目录 2：菜单 3：按钮")
	private Integer type;

	/**
	 * 组件
	 */
	@ExcelProperty(value = "组件", index = 9)
	@ApiModelProperty("组件")
	private String component;

	/**
	 * url地址
	 */
	@ExcelProperty(value = "url地址", index = 10)
	@ApiModelProperty("url地址")
	private String url;
}
