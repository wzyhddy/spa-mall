package com.net.sparrow.entity.sys;

import com.alibaba.excel.annotation.ExcelProperty;
import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 部门实体
 * 
 * @author sparrow
 * @date 2025-02-17 20:14:34
 */
@ApiModel("部门实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeptEntity extends BaseEntity {
	

	/**
	 * 名称
	 */
	@ApiModelProperty("名称")
	@ExcelProperty(value = "部门名称", index = 0)
	private String name;

	/**
	 * 上级部门ID
	 */
	@ApiModelProperty("上级部门ID")
	@ExcelProperty(value = "上级部门ID", index = 1)
	private Long pid;

	/**
	 * 有效状态 1:有效 0:无效
	 */
	@ApiModelProperty("有效状态 1:有效 0:无效")
	@ExcelProperty(value = "有效状态", index = 2)
	private Integer validStatus;
}
