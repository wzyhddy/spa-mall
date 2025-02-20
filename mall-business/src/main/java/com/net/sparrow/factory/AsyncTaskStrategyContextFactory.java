package com.net.sparrow.factory;

import com.net.sparrow.service.task.IAsyncTask;
import com.net.sparrow.util.AssertUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * @Author: Sparrow
 * @Description: 策略工厂类
 * 这个类实现了一个单例的工厂方法，同时支持通过不同的taskType获取不同的实体。
 * @DateTime: 2025/2/20 16:07
 **/
public class AsyncTaskStrategyContextFactory {
	/**
	 * 策略实体映射
	 */
	private static Map<Integer, IAsyncTask> asyncTaskMap;

	private static final AsyncTaskStrategyContextFactory INSTANCE = new AsyncTaskStrategyContextFactory();

	private AsyncTaskStrategyContextFactory() {

	}

	public static AsyncTaskStrategyContextFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化策略类
	 * @param map 实例映射
	 */
	public void initAsyncTaskMap(Map<Integer, IAsyncTask> map) {
		if (MapUtils.isEmpty(asyncTaskMap)) {
			asyncTaskMap = map;
		}
	}

	/**
	 * 根据任务类型获取策略实体对象
	 * @param taskType 任务类型
	 * @return 策略实体对象
	 */
	public IAsyncTask getStrategy(Integer taskType) {
		AssertUtil.notNull(taskType, "任务类型不能为空");
		AssertUtil.isTrue(MapUtils.isNotEmpty(asyncTaskMap), "asyncTaskMap不能为空");
		return asyncTaskMap.get(taskType);
	}


}
