package com.net.sparrow.util;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: Sparrow
 * @Description: api请求响应实体
 * @DateTime: 2025/2/17 21:55
 **/
@AllArgsConstructor
@Data
public class ApiResult<T> {

	/**
	 * 请求成功状态码
	 */
	public static final int OK = HttpStatus.HTTP_OK;

	/**
	 * 接口返回码
	 */
	private int code;

	/**
	 * 接口返回信息
	 */
	private String message;

	/**
	 * 数据
	 */
	private T data;
}
