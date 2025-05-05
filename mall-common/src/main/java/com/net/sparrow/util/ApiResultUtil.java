package com.net.sparrow.util;

/**
 * @Author: Sparrow
 * @Description: api请求响应实体处理工具类
 * @DateTime: 2025/2/17 21:57
 **/
public class ApiResultUtil {

	private ApiResultUtil() {

	}

	/**
	 * 请求成功
	 *
	 * @param data 数据
	 * @param <T>  数据类型
	 * @return 接口相应实体
	 */
	public static <T> ApiResult<T> success(T data) {
		return new ApiResult<>(ApiResult.OK, null, data);
	}

	/**
	 * 请求成功
	 *
	 * @param <T> 数据类型
	 * @return 接口相应实体
	 */
	public static <T> ApiResult<T> success() {
		return success(null);
	}
	/**
	 * 请求失败
	 * @param code    返回码
	 * @param message 返回信息
	 * @param <T>     数据类型
	 * @return 接口相应实体
	 */
	public static <T> ApiResult<T> error(int code, String message) {
		return new ApiResult<>(code, message, null);
	}
}
