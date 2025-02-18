package com.net.sparrow.entity.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 权限用户实体
 */
@ApiModel("权限用户实体")
@Data
public class AuthUserEntity {

    /**
     * 唯一标识
     */
    @ApiModelProperty("唯一标识")
    private String uuid;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @NotBlank
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String code;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
}
