package com.net.sparrow.entity.mall;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品图片实体
 * 
 * @author sparrow
 * @date 2025-02-22 20:55:27
 */
@ApiModel("商品图片实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductPhotoEntity extends BaseEntity {
	

	/**
	 * 商品ID
	 */
	@ApiModelProperty("商品ID")
	private Long productId;

	/**
	 * 图片名称
	 */
	@ApiModelProperty("图片名称")
	private String name;

	/**
	 * 图片url
	 */
	@ApiModelProperty("图片url")
	private String url;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer sort;
}
