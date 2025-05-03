package com.net.sparrow.entity.mall;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品属性实体
 * 
 * @author sparrow
 * @date 2025-02-22 20:55:27
 */
@ApiModel("商品属性实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductAttributeEntity extends BaseEntity {
	

	/**
	 * 商品ID
	 */
	@ApiModelProperty("商品ID")
	private Long productId;

	/**
	 * 属性ID
	 */
	@ApiModelProperty("属性ID")
	private Long attributeId;

	/**
	 * 属性值ID
	 */
	@ApiModelProperty("属性值ID")
	private Long attributeValueId;
}
