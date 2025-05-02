package com.net.sparrow.factory;

import com.net.sparrow.service.task.IAsyncTask;
import com.net.sparrow.util.AssertUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * @Author: Sparrow
 * @Description: 定时任务策略工厂
 * @DateTime: 2025/5/2 18:35
 **/
public class AsyncTaskStrategyContextFactory {

	//策略实体映射
	private static Map<Integer, IAsyncTask> asyncTaskMap;

	private static AsyncTaskStrategyContextFactory INSTANCE = new AsyncTaskStrategyContextFactory();

	public AsyncTaskStrategyContextFactory() {

	}

	public static AsyncTaskStrategyContextFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化策略类
	 * @param map
	 */
	public void initAsyncTaskMap(Map<Integer, IAsyncTask> map) {
		if (MapUtils.isEmpty(asyncTaskMap)) {
			asyncTaskMap = map;
		}
	}

	public IAsyncTask getStrategy(Integer taskType) {
		AssertUtil.notNull(taskType, "任务类型不能为空");
		AssertUtil.isTrue(MapUtils.isNotEmpty(asyncTaskMap), "asyncTaskMap不能为空");
		return asyncTaskMap.get(taskType);
	}
}
