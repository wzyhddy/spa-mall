package com.net.sparrow.handler;

import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.util.ApiResult;
import com.net.sparrow.util.ApiResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: Sparrow
 * @Description: 全局异常处理器
 * @DateTime: 2025/2/17 22:08
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Throwable.class)
	public ApiResult handleException(Throwable e) {
		if (e instanceof BusinessException) {
			BusinessException businessException = (BusinessException) e;
			log.info("请求出现业务异常：", e);
			return ApiResultUtil.error(businessException.getCode(), businessException.getMessage());
		}
		log.error("请求出现系统异常：", e);
		return ApiResultUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误");
	}
}
