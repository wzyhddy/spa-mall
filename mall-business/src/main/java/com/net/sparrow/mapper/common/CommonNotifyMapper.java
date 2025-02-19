package com.net.sparrow.mapper.common;

import com.net.sparrow.entity.common.CommonNotifyConditionEntity;
import com.net.sparrow.entity.common.CommonNotifyEntity;

import java.util.List;

/**
 * 通知 mapper
 */
public interface CommonNotifyMapper {
	/**
     * 查询通知信息
     *
     * @param id 通知ID
     * @return 通知信息
     */
	CommonNotifyEntity findById(Long id);

	/**
     * 根据条件查询通知列表
     *
     * @param commonNotifyConditionEntity 通知信息
     * @return 通知集合
     */
	List<CommonNotifyEntity> searchByCondition(CommonNotifyConditionEntity commonNotifyConditionEntity);

	/**
	 * 根据条件查询通知数量
	 *
	 * @param commonNotifyConditionEntity 通知信息
	 * @return 通知集合
	 */
	int searchCount(CommonNotifyConditionEntity commonNotifyConditionEntity);

	/**
     * 添加通知
     *
     * @param commonNotifyEntity 通知信息
     * @return 结果
     */
	int insert(CommonNotifyEntity commonNotifyEntity);

	/**
     * 修改通知
     *
     * @param commonNotifyEntity 通知信息
     * @return 结果
     */
	int update(CommonNotifyEntity commonNotifyEntity);

	/**
     * 删除通知
     *
     * @param id 通知ID
     * @return 结果
     */
	int deleteById(Long id);

}
