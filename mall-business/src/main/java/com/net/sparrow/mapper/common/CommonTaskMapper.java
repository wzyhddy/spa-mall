package com.net.sparrow.mapper.common;

import com.net.sparrow.entity.common.CommonTaskConditionEntity;
import com.net.sparrow.entity.common.CommonTaskEntity;

import java.util.List;

public interface CommonTaskMapper {
	/**
     * 查询任务信息
     *
     * @param id 任务ID
     * @return 任务信息
     */
	CommonTaskEntity findById(Long id);

	/**
     * 根据条件查询任务列表
     *
     * @param commonTaskConditionEntity 任务信息
     * @return 任务集合
     */
	List<CommonTaskEntity> searchByCondition(CommonTaskConditionEntity commonTaskConditionEntity);

	/**
	 * 根据条件查询任务数量
	 *
	 * @param commonTaskConditionEntity 任务信息
	 * @return 任务集合
	 */
	int searchCount(CommonTaskConditionEntity commonTaskConditionEntity);

	/**
     * 添加任务
     *
     * @param commonTaskEntity 任务信息
     * @return 结果
     */
	int insert(CommonTaskEntity commonTaskEntity);

	/**
     * 修改任务
     *
     * @param commonTaskEntity 任务信息
     * @return 结果
     */
	int update(CommonTaskEntity commonTaskEntity);

	/**
     * 删除任务
     *
     * @param id 任务ID
     * @return 结果
     */
	int deleteById(Long id);

}
