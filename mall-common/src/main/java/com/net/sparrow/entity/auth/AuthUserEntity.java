package com.net.sparrow.entity.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 权限用户实体
 *
 * @author 苏三，该项目是知识星球：java突击队 的内部项目
 * @date 2024/1/9 下午5:09
 */
@ApiModel("权限用户实体")
@Data
public class AuthUserEntity {

    /**
     * 唯一标识
     */
    @NotBlank(message = "唯一标识不能为空")
    @ApiModelProperty("唯一标识")
    private String uuid;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空")
    @ApiModelProperty("用户名称")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    @NotBlank
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String code;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
}
