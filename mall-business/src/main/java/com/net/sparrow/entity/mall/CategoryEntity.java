package com.net.sparrow.entity.mall;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分类实体
 * 
 * @author sparrow
 * @date 2025-02-22 20:55:27
 */
@ApiModel("分类实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryEntity extends BaseEntity {
	

	/**
	 * 父分类ID
	 */
	@ApiModelProperty("父分类ID")
	private Long parentId;

	/**
	 * 定时任务名称
	 */
	@ApiModelProperty("定时任务名称")
	private String name;

	/**
	 * 层级
	 */
	@ApiModelProperty("层级")
	private Integer level;

	/**
	 * 是否叶子节点
	 */
	@ApiModelProperty("是否叶子节点")
	private Integer isLeaf;
}
