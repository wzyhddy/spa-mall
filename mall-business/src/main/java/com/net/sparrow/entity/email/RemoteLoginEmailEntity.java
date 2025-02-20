package com.net.sparrow.entity.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemoteLoginEmailEntity implements Serializable {

    /**
     * 收件人邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 登录地点
     */
    private String cityName;

    /**
     * 登录时间
     */
    private String loginTime;


    /**
     * 设备
     */
    private String device;

    /**
     * IP地址
     */
    private String ip;
}