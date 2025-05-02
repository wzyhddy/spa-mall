package com.net.sparrow.listener;

import com.net.sparrow.annotation.AsyncTask;
import com.net.sparrow.factory.AsyncTaskStrategyContextFactory;
import com.net.sparrow.service.task.IAsyncTask;
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
 * @Description: 应用监听器
 * @DateTime: 2025/5/2 18:41
 **/
@Component
public class TaskApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		Map<String, Object> beansWithMap = applicationContext.getBeansWithAnnotation(AsyncTask.class);
		int initSize = MapUtils.isEmpty(beansWithMap) ? 0 : beansWithMap.size();
		Map<Integer, IAsyncTask> handlerMap = new HashMap<>(initSize);
		if (MapUtils.isNotEmpty(beansWithMap)) {
			beansWithMap.forEach((beanName, object) -> {
				AsyncTask taskTypeEnum = object.getClass().getAnnotation(AsyncTask.class);
				if (Objects.nonNull(taskTypeEnum)) {
					handlerMap.put(taskTypeEnum.value().getValue(), (IAsyncTask) object);
				}
			});
			AsyncTaskStrategyContextFactory.getInstance().initAsyncTaskMap(handlerMap);
		}

	}
}
