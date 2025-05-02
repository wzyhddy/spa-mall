package com.net.sparrow.job;

import com.net.sparrow.entity.common.CommonTaskConditionEntity;
import com.net.sparrow.entity.common.CommonTaskEntity;
import com.net.sparrow.enums.TaskStatusEnum;
import com.net.sparrow.factory.AsyncTaskStrategyContextFactory;
import com.net.sparrow.mapper.common.CommonTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Sparrow
 * @Description: CommonTaskJob
 * @DateTime: 2025/5/1 18:57
 **/
@Slf4j
@Component
public class CommonTaskJob {

	private static final List<Integer> QUERY_VALID_STATUS_LIST = new ArrayList<>();

	static {
		QUERY_VALID_STATUS_LIST.add(TaskStatusEnum.WAITING.getValue());
		QUERY_VALID_STATUS_LIST.add(TaskStatusEnum.RUNNING.getValue());
	}

	@Autowired
	private CommonTaskMapper commonTaskMapper;

	@Scheduled(fixedRate = 10000)
	public void run() {
		CommonTaskConditionEntity conditionEntity = new CommonTaskConditionEntity();
		conditionEntity.setStatusList(QUERY_VALID_STATUS_LIST);
		List<CommonTaskEntity> commonTaskEntities = commonTaskMapper.searchByCondition(conditionEntity);
		if (CollectionUtils.isEmpty(commonTaskEntities)) {
			log.info("没有任务需要执行");
			return;
		}
		for (CommonTaskEntity commonTaskEntity : commonTaskEntities) {
			AsyncTaskStrategyContextFactory.getInstance().getStrategy(commonTaskEntity.getType()).doTask(commonTaskEntity);
		}
	}
}
