package com.net.sparrow.entity.mall;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 品牌实体
 * 
 * @author sparrow
 * @date 2025-02-22 20:55:27
 */
@ApiModel("品牌实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BrandEntity extends BaseEntity {
	

	/**
	 * 品牌名称
	 */
	@ApiModelProperty("品牌名称")
	private String name;
}
