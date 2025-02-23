package com.net.sparrow.entity.mall;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 单位实体
 * 
 * @author sparrow
 * @date 2025-02-22 20:55:27
 */
@ApiModel("单位实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnitEntity extends BaseEntity {
	

	/**
	 * 单位名称
	 */
	@ApiModelProperty("单位名称")
	private String name;
}
