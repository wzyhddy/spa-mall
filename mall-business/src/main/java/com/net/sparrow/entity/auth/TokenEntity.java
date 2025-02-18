package com.net.sparrow.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * token实体
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenEntity {

    /**
     * 用户名称
     */
    private String username;

    /**
     * token
     */
    private String token;

    /**
     *角色信息
     */
    private List<String> roles;
}
