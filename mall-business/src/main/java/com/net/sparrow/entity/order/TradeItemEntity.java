package com.net.sparrow.entity.order;

import com.net.sparrow.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单明细实体
 */
@ApiModel("订单明细实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeItemEntity extends BaseEntity {


    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long tradeId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty("商品ID")
    private Long productId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * 商品规格
     */
    @ApiModelProperty("商品规格")
    private String model;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    @ApiModelProperty("单价")
    private BigDecimal price;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @ApiModelProperty("数量")
    private Integer quantity;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @ApiModelProperty("金额")
    private BigDecimal amount;
}
