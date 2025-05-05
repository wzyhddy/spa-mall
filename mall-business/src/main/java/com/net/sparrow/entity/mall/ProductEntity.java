package com.net.sparrow.entity.mall;

import com.net.sparrow.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 商品实体
 * 
 * @author sparrow
 * @date 2025-02-22 20:55:27
 */
@ApiModel("商品实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductEntity extends BaseEntity {
	/**
	 * 分类ID
	 */
	@NotNull(message = "分类ID不能为空")
	@ApiModelProperty("分类ID")
	private Long categoryId;

	/**
	 * 品牌ID
	 */
	@NotNull(message = "品牌ID不能为空")
	@ApiModelProperty("品牌ID")
	private Long brandId;

	/**
	 * 单位ID
	 */
	@NotNull(message = "单位ID不能为空")
	@ApiModelProperty("单位ID")
	private Long unitId;

	/**
	 * 商品名称
	 */
	@NotEmpty(message = "商品名称不能为空")
	@ApiModelProperty("商品名称")
	private String name;

	/**
	 * 规格
	 */
	@ApiModelProperty("规格")
	private String model;

	/**
	 * 规格hash值
	 */
	@ApiModelProperty("规格hash值")
	private String hash;

	/**
	 * 数量
	 */
	@NotNull(message = "数量不能为空")
	@ApiModelProperty("数量")
	private Integer quantity;

	/**
	 * 价格
	 */
	@NotNull(message = "数量不能为空")
	@ApiModelProperty("价格")
	private BigDecimal price;

	/**
	 * 是否新创建的商品
	 */
	private Boolean isNew;
	/**
	 * 属性集合
	 */
	@Size(message = "属性集合不能为空")
	@ApiModelProperty("属性集合")
	private List<AttributeValueEntity> attributeEntityList;

	/**
	 * 商品图片
	 */
	@ApiModelProperty("商品图片")
	private List<ProductPhotoEntity> productPhotoEntityList;

	/**
	 * 封面图片
	 */
	private List<String> cover;

	/**
	 * 轮播图
	 */
	private List<String> swiper;

	/**
	 * 详情
	 */
	private String detail;
}
