package com.net.sparrow.handler;

import com.net.sparrow.entity.common.CommonTaskConditionEntity;
import com.net.sparrow.entity.common.CommonTaskEntity;
import com.net.sparrow.enums.JobResult;
import com.net.sparrow.enums.TaskStatusEnum;
import com.net.sparrow.factory.AsyncTaskStrategyContextFactory;
import com.net.sparrow.mapper.common.CommonTaskMapper;
import com.net.sparrow.service.task.IAsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Sparrow
 * @DateTime: 2025/2/20 16:49
 **/
@Slf4j
@Component
public class CommonTaskJob extends BaseJob {


	private static final List<Integer> QUERY_VALID_STATUS_LIST = new ArrayList<>();

	static {
		QUERY_VALID_STATUS_LIST.add(TaskStatusEnum.WAITING.getValue());
		QUERY_VALID_STATUS_LIST.add(TaskStatusEnum.RUNNING.getValue());
	}

	@Autowired
	private CommonTaskMapper commonTaskMapper;

	public void doRun() {
		CommonTaskConditionEntity commonTaskConditionEntity = new CommonTaskConditionEntity();
		commonTaskConditionEntity.setStatusList(QUERY_VALID_STATUS_LIST);
		List<CommonTaskEntity> commonTaskEntities = commonTaskMapper.searchByCondition(commonTaskConditionEntity);
		if (CollectionUtils.isEmpty(commonTaskEntities)) {
			log.info("没有任务需要执行");
			return;
		}

		/**
		 * 根据不同的任务类型，去找到不同的service，然后执行它的doTask方法。
		 */
		for (CommonTaskEntity commonTaskEntity : commonTaskEntities) {
			IAsyncTask strategy = AsyncTaskStrategyContextFactory.getInstance().getStrategy(commonTaskEntity.getType());
			strategy.doTask(commonTaskEntity);
		}
	}

	@Override
	public JobResult doRun(String params) {
		doRun();
		return JobResult.SUCCESS;
	}
}
