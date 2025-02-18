package com.net.sparrow.util;

import com.net.sparrow.constant.NumberConstant;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * token处理工具
 */
public abstract class TokenUtil {

    private static final String AUTHORIZATION_PREFIX = "Basic";
    private static final String AUTHORIZATION_SEPARATE = "@";


    private TokenUtil() {
    }

    /**
     * 从authorization中解析token
     * <p>
     * authorization字符串是下面这样的：
     * Basic eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXNhbiIsImV4cCI6MTcwNTAzOTA3N30.DZV6CZYGla74CZaXU1sqnX9R_x5YxfTM-DWObURn3Uhr1E88XsOxOz8F_MDfh8AaVFm87zlGXAENC8soZNz0Qw
     *
     * @param request 用户请求
     * @return token
     */
    public static String getTokenForAuthorization(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasLength(authorization) || !authorization.contains(AUTHORIZATION_PREFIX) || !authorization.contains(AUTHORIZATION_SEPARATE)) {
            return null;
        }
        String[] values = authorization.split(AUTHORIZATION_SEPARATE);
        if (values.length != NumberConstant.NUMBER_2) {
            return null;
        }
        return values[1];
    }
}
