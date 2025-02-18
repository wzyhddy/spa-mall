package com.net.sparrow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 菜单树实体
 */
@Data
public class MenuTreeDTO implements Serializable {

    /**
     * 菜单系统ID
     */
    @ApiModelProperty("菜单系统ID")
    private Long id;

    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String name;

    /**
     * 上级菜单ID
     */
    @ApiModelProperty("上级菜单ID")
    private Long pid;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 路由
     */
    @ApiModelProperty("路由")
    private String path;

    /**
     * 是否隐藏
     */
    @ApiModelProperty("是否隐藏")
    private Boolean hidden;

    /**
     * 是否外链 1：是 0：否
     */
    @ApiModelProperty("是否外链 1：是 0：否")
    private Integer isLink;

    /**
     * 类型 1：目录 2：菜单 3：按钮
     */
    @ApiModelProperty("类型 1：目录 2：菜单 3：按钮")
    private Integer type;

    /**
     * 功能权限
     */
    @ApiModelProperty("功能权限")
    private String permission;

    /**
     * url地址
     */
    @ApiModelProperty("url地址")
    private String url;

    /**
     * 组件
     */
    @ApiModelProperty("组件")
    private String component;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 跳转
     */
    private String redirect;

//    一级菜单返回的是true表示一直显示，而其他级的菜单，该字段是false，需要默认是不展示的，需要手动点击触发展开
    private Boolean alwaysShow;


    private MetaDTO meta;

    /**
     * 下一级菜单集合
     */
    private List<MenuTreeDTO> children;


    /**
     * 增加添加子菜单的方法
     *
     * @param menuTreeDTO 子菜单
     */
    public void addChildren(MenuTreeDTO menuTreeDTO) {
        if (Objects.isNull(children)) {
            children = Lists.newArrayList();
        }
        children.add(menuTreeDTO);
    }
}
