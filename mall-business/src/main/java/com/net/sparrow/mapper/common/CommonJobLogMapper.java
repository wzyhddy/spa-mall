package com.net.sparrow.mapper.common;

import com.net.sparrow.entity.common.CommonJobLogConditionEntity;
import com.net.sparrow.entity.common.CommonJobLogEntity;
import com.net.sparrow.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定时任务执行日志 mapper
 */
public interface CommonJobLogMapper extends BaseMapper<CommonJobLogEntity, CommonJobLogConditionEntity> {
	/**
     * 查询定时任务执行日志信息
     *
     * @param id 定时任务执行日志ID
     * @return 定时任务执行日志信息
     */
	CommonJobLogEntity findById(Long id);

	/**
     * 添加定时任务执行日志
     *
     * @param commonJobLogEntity 定时任务执行日志信息
     * @return 结果
     */
	int insert(CommonJobLogEntity commonJobLogEntity);

	/**
     * 修改定时任务执行日志
     *
     * @param commonJobLogEntity 定时任务执行日志信息
     * @return 结果
     */
	int update(CommonJobLogEntity commonJobLogEntity);

	/**
     * 批量删除定时任务执行日志
     *
     * @param ids id集合
     * @param entity 定时任务执行日志实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CommonJobLogEntity entity);

	/**
     * 批量查询定时任务执行日志信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<CommonJobLogEntity> findByIds(List<Long> ids);
}
