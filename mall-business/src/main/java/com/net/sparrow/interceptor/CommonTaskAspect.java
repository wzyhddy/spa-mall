package com.net.sparrow.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.net.sparrow.annotation.ExcelExport;
import com.net.sparrow.entity.common.CommonTaskEntity;
import com.net.sparrow.enums.ExcelBizTypeEnum;
import com.net.sparrow.enums.TaskStatusEnum;
import com.net.sparrow.enums.TaskTypeEnum;
import com.net.sparrow.mapper.common.CommonTaskMapper;
import com.net.sparrow.util.FillUserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: Sparrow
 * @Description: TODO
 * @DateTime: 2025/5/1 16:25
 **/
@Aspect
@Component
public class CommonTaskAspect {

	@Autowired
	private CommonTaskMapper commonTaskMapper;

	@Pointcut("@annotation(com.net.sparrow.annotation.ExcelExport)")
	public void pointCut() {

	}

	@Before("pointCut()")
	public void before(JoinPoint joinPoint) throws Throwable {
		String targetName = joinPoint.getTarget().getClass().getName();
		Class<?> targetClass = Class.forName(targetName);
		//获取切入方法名
		String methodName = joinPoint.getSignature().getName();
		//获取切入方法参数
		Object[] arguments = joinPoint.getArgs();
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			// 方法名相同、包含目标注解、方法参数个数相同（避免有重载）
			if(method.getName().equals(methodName) && method.isAnnotationPresent(ExcelExport.class) && method.getParameterTypes().length == arguments.length) {
				ExcelBizTypeEnum excelBizTypeEnum = method.getAnnotation(ExcelExport.class).value();
				CommonTaskEntity commonTaskEntity = createCommonTaskEntity(excelBizTypeEnum);
				if(ArrayUtil.isNotEmpty(arguments)) {
					Object requestParam = arguments[1];
					commonTaskEntity.setRequestParam(JSONUtil.toJsonStr(requestParam));
				}
				commonTaskMapper.insert(commonTaskEntity);
			}
		}
	}

	private CommonTaskEntity createCommonTaskEntity(ExcelBizTypeEnum excelBizTypeEnum) {
		CommonTaskEntity commonTaskEntity = new CommonTaskEntity();
		commonTaskEntity.setName(getTaskName(excelBizTypeEnum));
		commonTaskEntity.setStatus(TaskStatusEnum.WAITING.getValue());
		commonTaskEntity.setFailureCount(0);
		commonTaskEntity.setType(TaskTypeEnum.MENU.getValue());
		commonTaskEntity.setBizType(excelBizTypeEnum.getValue());
		FillUserUtil.fillCreateUserInfo(commonTaskEntity);
		return commonTaskEntity;
	}

	private String getTaskName(ExcelBizTypeEnum excelBizTypeEnum) {
		return String.format("导出%s数据", excelBizTypeEnum.getDesc());
	}
}
