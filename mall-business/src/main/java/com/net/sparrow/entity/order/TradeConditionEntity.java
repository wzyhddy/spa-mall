package com.net.sparrow.entity.order;

import com.net.sparrow.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单查询条件实体
 */
@ApiModel("订单查询条件实体")
@Data
public class TradeConditionEntity extends RequestPageEntity {


	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  订单编码
     */
	@ApiModelProperty("订单编码")
	private String code;

	/**
	 *  用户ID
     */
	@ApiModelProperty("用户ID")
	private Long userId;

	/**
	 *  用户名称
     */
	@ApiModelProperty("用户名称")
	private String userName;

	/**
	 *  下单时间
     */
	@ApiModelProperty("下单时间")
	private Date orderTime;

	/**
	 *  订单状态 1:下单 2:支付 3：完成 4：取消
     */
	@ApiModelProperty("订单状态 1:下单 2:支付 3：完成 4：取消")
	private Integer orderStatus;

	/**
	 *  支付状态 1:待支付 2:已支付 3：退款
     */
	@ApiModelProperty("支付状态 1:待支付 2:已支付 3：退款")
	private Integer payStatus;

	/**
	 *  总金额
     */
	@ApiModelProperty("总金额")
	private BigDecimal totalAmount;

	/**
	 *  付款金额
     */
	@ApiModelProperty("付款金额")
	private BigDecimal paymentAmount;

	/**
	 *  备注
     */
	@ApiModelProperty("备注")
	private String remark;

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
