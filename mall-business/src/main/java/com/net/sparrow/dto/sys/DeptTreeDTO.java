package com.net.sparrow.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.compress.utils.Lists;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@ApiModel("部门树实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeptTreeDTO {

    /**
     * 系统ID
     */
    @ApiModelProperty("系统ID")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 前端页面显示用的标签名称
     */
    @ApiModelProperty("标签名称")
    private String label;

    /**
     * 上级部门
     */
    @ApiModelProperty("上级部门")
    private Long pid;

    /**
     * 有效状态 1:有效 0:无效
     */
    @ApiModelProperty("有效状态 1:有效 0:无效")
    private Integer validStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 子部门
     */
    private List<DeptTreeDTO> children;

    /**
     * 增加添加子部门的方法
     *
     * @param deptTreeDTO 子部门
     */
    public void addChildren(DeptTreeDTO deptTreeDTO) {
        if (Objects.isNull(children)) {
            children = Lists.newArrayList();
        }
        children.add(deptTreeDTO);
    }
}