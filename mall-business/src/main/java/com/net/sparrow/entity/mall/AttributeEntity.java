package com.net.sparrow.entity.mall;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 属性实体
 * 
 * @author sparrow
 * @date 2025-02-22 20:55:27
 */
@ApiModel("属性实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttributeEntity extends BaseEntity {
	

	/**
	 * 属性名称
	 */
	@ApiModelProperty("属性名称")
	private String name;
}
