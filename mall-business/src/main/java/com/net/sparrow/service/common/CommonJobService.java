package com.net.sparrow.service.common;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.common.CommonJobConditionEntity;
import com.net.sparrow.entity.common.CommonJobEntity;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.mapper.common.CommonJobMapper;
import com.net.sparrow.service.BaseService;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 定时任务 服务层
 */
@Service
public class CommonJobService extends BaseService< CommonJobEntity,  CommonJobConditionEntity> {

	@Autowired
	private CommonJobMapper commonJobMapper;

	/**
     * 查询定时任务信息
     *
     * @param id 定时任务ID
     * @return 定时任务信息
     */
	public CommonJobEntity findById(Long id) {
	    return commonJobMapper.findById(id);
	}

	/**
     * 根据条件分页查询定时任务列表
     *
     * @param commonJobConditionEntity 定时任务信息
     * @return 定时任务集合
     */
	public ResponsePageEntity<CommonJobEntity> searchByPage(CommonJobConditionEntity commonJobConditionEntity) {
		int count = commonJobMapper.searchCount(commonJobConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(commonJobConditionEntity);
		}
		List<CommonJobEntity> dataList = commonJobMapper.searchByCondition(commonJobConditionEntity);
		return ResponsePageEntity.build(commonJobConditionEntity, count, dataList);
	}

    /**
     * 新增定时任务
     *
     * @param commonJobEntity 定时任务信息
     * @return 结果
     */
	public int insert(CommonJobEntity commonJobEntity) {
	    return commonJobMapper.insert(commonJobEntity);
	}

	/**
     * 修改定时任务
     *
     * @param commonJobEntity 定时任务信息
     * @return 结果
     */
	public int update(CommonJobEntity commonJobEntity) {
	    return commonJobMapper.update(commonJobEntity);
	}

	/**
     * 批量删除定时任务对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<CommonJobEntity> entities = commonJobMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "定时任务已被删除");

		CommonJobEntity entity = new CommonJobEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return commonJobMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return commonJobMapper;
	}

}
