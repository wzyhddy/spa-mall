package com.net.sparrow.entity.sys;

import com.net.sparrow.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门实体
 */
@ApiModel("数据字典详情实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DictDetailEntity extends BaseEntity {


    /**
     * 数据字典id
     */
    @ApiModelProperty("数据字典id")
    private Long dictId;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 文本
     */
    @ApiModelProperty("文本")
    private String label;

    /**
     * 数据字典
     */
    @ApiModelProperty("数据字典")
    private DictEntity dict;
}
