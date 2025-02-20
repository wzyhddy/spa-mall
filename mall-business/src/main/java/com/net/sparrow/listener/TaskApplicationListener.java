package com.net.sparrow.listener;

import com.net.sparrow.annotation.AsyncTask;
import com.net.sparrow.factory.AsyncTaskStrategyContextFactory;
import com.net.sparrow.service.task.IAsyncTask;
import com.sun.javafx.collections.MappingChange;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Sparrow
 * @Description: 监听器
 * @DateTime: 2025/2/20 16:25
 **/
@Component
public class TaskApplicationListener implements ApplicationListener<ContextRefreshedEvent> {


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		Map<String, Object> beanWithMap = applicationContext.getBeansWithAnnotation(AsyncTask.class);
		int initSize = MapUtils.isEmpty(beanWithMap) ? 0 : beanWithMap.size();
		HashMap<Integer, IAsyncTask> handlerMap = new HashMap<>(initSize);
		if (MapUtils.isNotEmpty(beanWithMap)) {
			beanWithMap.forEach((beanName,object)->{
				AsyncTask taskTypeEnum = object.getClass().getAnnotation(AsyncTask.class);
				if (Objects.nonNull(taskTypeEnum)) {
					handlerMap.put(taskTypeEnum.value().getValue(), (IAsyncTask) object);
				}
			});
			AsyncTaskStrategyContextFactory.getInstance().initAsyncTaskMap(handlerMap);
		}
	}
}
