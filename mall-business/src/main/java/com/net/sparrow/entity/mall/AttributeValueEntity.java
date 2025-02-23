package com.net.sparrow.entity.mall;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 属性值实体
 * 
 * @author sparrow
 * @date 2025-02-22 20:55:27
 */
@ApiModel("属性值实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttributeValueEntity extends BaseEntity {
	

	/**
	 * 属性ID
	 */
	@ApiModelProperty("属性ID")
	private Long attributeId;

	/**
	 * 属性值
	 */
	@ApiModelProperty("属性值")
	private String value;

	/**
	 * 属性名称
	 */
	@ApiModelProperty("属性名称")
	private String attributeName;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer sort;
}
