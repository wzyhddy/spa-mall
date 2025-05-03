package com.net.sparrow.entity.order;

import com.net.sparrow.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单实体
 */
@ApiModel("订单实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeEntity extends BaseEntity {


    /**
     * 订单编码
     */
    @ApiModelProperty("订单编码")
    private String code;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date orderTime;

    /**
     * 订单状态 1:下单 2:支付 3：完成 4：取消
     */
    @ApiModelProperty("订单状态 1:下单 2:支付 3：完成 4：取消")
    private Integer orderStatus;

    /**
     * 支付状态 1:待支付 2:已支付 3：退款
     */
    @ApiModelProperty("支付状态 1:待支付 2:已支付 3：退款")
    private Integer payStatus;

    /**
     * 总金额
     */
    @NotNull(message = "总金额不能为空")
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    /**
     * 付款金额
     */
    @NotNull(message = "付款金额不能为空")
    @ApiModelProperty("付款金额")
    private BigDecimal paymentAmount;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


    /**
     * 订单明细
     */
    @Valid
    @NotNull(message = "订单明细不能为空")
    @ApiModelProperty("订单明细")
    private List<TradeItemEntity> tradeItemEntityList;
}
