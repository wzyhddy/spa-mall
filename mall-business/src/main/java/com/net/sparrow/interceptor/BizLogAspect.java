package com.net.sparrow.interceptor;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.net.sparrow.annotation.BizLog;
import com.net.sparrow.entity.log.BizLogEntity;
import com.net.sparrow.service.log.BizLogService;
import com.net.sparrow.util.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Author: Sparrow
 * @Description: 用户请求日志拦截器
 * @DateTime: 2025/2/20 10:02
 **/
@Aspect
@Component
public class BizLogAspect {

	@Autowired
	private BizLogService bizLogService;

	@Pointcut("@annotation(com.net.sparrow.annotation.BizLog)")
	public void pointcut() {

	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Object result = joinPoint.proceed();
		long time = System.currentTimeMillis() - startTime;
		BizLogEntity bizLogEntity = createBizLogEntity(joinPoint, httpServletRequest);
		bizLogEntity.setTime((int) time);
		bizLogEntity.setStatus(1);
		bizLogService.save(bizLogEntity);
		return result;
	}

	private BizLogEntity createBizLogEntity(ProceedingJoinPoint joinPoint, HttpServletRequest httpServletRequest) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		BizLog bizLog = method.getAnnotation(BizLog.class);
		String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName();

		BizLogEntity bizLogEntity = new BizLogEntity();
		bizLogEntity.setDescription(bizLog.value());
		bizLogEntity.setMethodName(methodName);
		bizLogEntity.setStatus(1);
		bizLogEntity.setRequestIp(IpUtil.getIpAddr(httpServletRequest));
		bizLogEntity.setUrl(httpServletRequest.getRequestURI());
		bizLogEntity.setBrowser(getBrowserName(httpServletRequest));
		bizLogEntity.setParam(getParam(joinPoint));
		return bizLogEntity;
	}

	private String getBrowserName(HttpServletRequest httpServletRequest) {
		String userAgentString = httpServletRequest.getHeader("User-Agent");
		UserAgent ua = UserAgentUtil.parse(userAgentString);
		return ua.getBrowser().toString();
	}

	private String getParam(JoinPoint joinPoint) {
		StringBuilder params = new StringBuilder("{");
		Object[] argValues = joinPoint.getArgs();
		String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
		if (argValues != null) {
			for (int i = 0; i < argValues.length; i++) {
				params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
			}
		}
		return params.append("}").toString();
	}
}


