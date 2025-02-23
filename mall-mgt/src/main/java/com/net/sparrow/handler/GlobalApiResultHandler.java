package com.net.sparrow.handler;

import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.util.ApiResult;
import com.net.sparrow.util.ApiResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Sparrow
 * @Description: 全局响应封装
 * @DateTime: 2025/2/17 21:59
 **/
@Slf4j
@RestControllerAdvice
public class GlobalApiResultHandler implements ResponseBodyAdvice<Object> {

	/**
	 * supports方法决定了beforeBodyWrite方法是否执行，只有supports方法返回true时，beforeBodyWrite方法才会执行。
	 * @param returnType
	 * @param converterType
	 * @return
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		String requestURI = request.getRequestURI();
		return matchUrl(requestURI);
	}

	private boolean matchUrl(String requestURI) {
		if (StringUtils.isBlank(requestURI)) {
			return false;
		}
		return requestURI.contains("/v1");
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body instanceof ApiResult) {
			return (ApiResult) body;
		}
		return ApiResultUtil.success(body);
	}

}
