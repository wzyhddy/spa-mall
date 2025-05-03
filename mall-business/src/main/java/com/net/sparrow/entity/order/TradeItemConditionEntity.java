package com.net.sparrow.entity.order;

import com.net.sparrow.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细查询条件实体
 */
@ApiModel("订单明细查询条件实体")
@Data
public class TradeItemConditionEntity extends RequestPageEntity {


	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  订单ID
     */
	@ApiModelProperty("订单ID")
	private Long tradeId;

	/**
	 *  商品ID
     */
	@ApiModelProperty("商品ID")
	private Long productId;

	/**
	 *  商品名称
     */
	@ApiModelProperty("商品名称")
	private String productName;

	/**
	 *  商品规格
     */
	@ApiModelProperty("商品规格")
	private String model;

	/**
	 *  单价
     */
	@ApiModelProperty("单价")
	private BigDecimal price;

	/**
	 *  数量
     */
	@ApiModelProperty("数量")
	private Integer quantity;

	/**
	 *  金额
     */
	@ApiModelProperty("金额")
	private BigDecimal amount;

	/**
	 *  创建人ID
     */
	@ApiModelProperty("创建人ID")
	private Long createUserId;

	/**
	 *  创建人名称
     */
	@ApiModelProperty("创建人名称")
	private String createUserName;

	/**
	 *  创建日期
     */
	@ApiModelProperty("创建日期")
	private Date createTime;

	/**
	 *  修改人ID
     */
	@ApiModelProperty("修改人ID")
	private Long updateUserId;

	/**
	 *  修改人名称
     */
	@ApiModelProperty("修改人名称")
	private String updateUserName;

	/**
	 *  修改时间
     */
	@ApiModelProperty("修改时间")
	private Date updateTime;

	/**
	 *  是否删除 1：已删除 0：未删除
     */
	@ApiModelProperty("是否删除 1：已删除 0：未删除")
	private Integer isDel;
}
