package com.net.sparrow.mapper.log;

import com.net.sparrow.entity.log.BizLogConditionEntity;
import com.net.sparrow.entity.log.BizLogEntity;

import java.util.List;

/**
 * 业务日志 mapper
 */
public interface BizLogMapper {
	/**
     * 查询业务日志信息
     *
     * @param id 业务日志ID
     * @return 业务日志信息
     */
	BizLogEntity findById(Long id);

	/**
     * 根据条件查询业务日志列表
     *
     * @param bizLogConditionEntity 业务日志信息
     * @return 业务日志集合
     */
	List<BizLogEntity> searchByCondition(BizLogConditionEntity bizLogConditionEntity);

	/**
	 * 根据条件查询业务日志数量
	 *
	 * @param bizLogConditionEntity 业务日志信息
	 * @return 业务日志集合
	 */
	int searchCount(BizLogConditionEntity bizLogConditionEntity);

	/**
     * 添加业务日志
     *
     * @param bizLogEntity 业务日志信息
     * @return 结果
     */
	int insert(BizLogEntity bizLogEntity);

	/**
     * 修改业务日志
     *
     * @param bizLogEntity 业务日志信息
     * @return 结果
     */
	int update(BizLogEntity bizLogEntity);

	/**
     * 删除业务日志
     *
     * @param id 业务日志ID
     * @return 结果
     */
	int deleteById(Long id);

}
