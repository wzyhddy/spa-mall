package com.net.sparrow.service.common;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.common.CommonJobLogConditionEntity;
import com.net.sparrow.entity.common.CommonJobLogEntity;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.mapper.common.CommonJobLogMapper;
import com.net.sparrow.service.BaseService;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 定时任务执行日志 服务层
 */
@Service
public class CommonJobLogService extends BaseService< CommonJobLogEntity,  CommonJobLogConditionEntity> {

	@Autowired
	private CommonJobLogMapper commonJobLogMapper;

	/**
     * 查询定时任务执行日志信息
     *
     * @param id 定时任务执行日志ID
     * @return 定时任务执行日志信息
     */
	public CommonJobLogEntity findById(Long id) {
	    return commonJobLogMapper.findById(id);
	}

	/**
     * 根据条件分页查询定时任务执行日志列表
     *
     * @param commonJobLogConditionEntity 定时任务执行日志信息
     * @return 定时任务执行日志集合
     */
	public ResponsePageEntity<CommonJobLogEntity> searchByPage(CommonJobLogConditionEntity commonJobLogConditionEntity) {
		int count = commonJobLogMapper.searchCount(commonJobLogConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(commonJobLogConditionEntity);
		}
		List<CommonJobLogEntity> dataList = commonJobLogMapper.searchByCondition(commonJobLogConditionEntity);
		return ResponsePageEntity.build(commonJobLogConditionEntity, count, dataList);
	}

    /**
     * 新增定时任务执行日志
     *
     * @param commonJobLogEntity 定时任务执行日志信息
     * @return 结果
     */
	public int insert(CommonJobLogEntity commonJobLogEntity) {
	    return commonJobLogMapper.insert(commonJobLogEntity);
	}

	/**
     * 修改定时任务执行日志
     *
     * @param commonJobLogEntity 定时任务执行日志信息
     * @return 结果
     */
	public int update(CommonJobLogEntity commonJobLogEntity) {
	    return commonJobLogMapper.update(commonJobLogEntity);
	}

	/**
     * 批量删除定时任务执行日志对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<CommonJobLogEntity> entities = commonJobLogMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "定时任务执行日志已被删除");

		CommonJobLogEntity entity = new CommonJobLogEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return commonJobLogMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return commonJobLogMapper;
	}

}
