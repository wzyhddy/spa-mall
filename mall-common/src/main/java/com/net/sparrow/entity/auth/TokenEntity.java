package com.net.sparrow.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * token实体
 *
 * @author 苏三，该项目是知识星球：java突击队 的内部项目
 * @date 2024/1/12 下午12:54
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
     * 角色信息
     */
    private List<String> roles;
}
