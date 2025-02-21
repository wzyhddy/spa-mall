package com.net.sparrow.service.sys;

import java.util.List;

import com.net.sparrow.util.BetweenTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.sys.JobMapper;
import com.net.sparrow.entity.sys.JobConditionEntity;
import com.net.sparrow.entity.sys.JobEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.service.BaseService;
/**
 * 岗位 服务层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Service
public class JobService extends BaseService< JobEntity,  JobConditionEntity> {

	@Autowired
	private JobMapper jobMapper;

	/**
     * 查询岗位信息
     *
     * @param id 岗位ID
     * @return 岗位信息
     */
	public JobEntity findById(Long id) {
	    return jobMapper.findById(id);
	}

	/**
     * 根据条件分页查询岗位列表
     *
     * @param jobConditionEntity 岗位信息
     * @return 岗位集合
     */
	public ResponsePageEntity<JobEntity> searchByPage(JobConditionEntity jobConditionEntity) {
		BetweenTimeUtil.parseTime(jobConditionEntity);
		int count = jobMapper.searchCount(jobConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(jobConditionEntity);
		}
		List<JobEntity> dataList = jobMapper.searchByCondition(jobConditionEntity);
		return ResponsePageEntity.build(jobConditionEntity, count, dataList);
	}

    /**
     * 新增岗位
     *
     * @param jobEntity 岗位信息
     * @return 结果
     */
	public int insert(JobEntity jobEntity) {
		FillUserUtil.fillCreateUserInfo(jobEntity);
	    return jobMapper.insert(jobEntity);
	}

	/**
     * 修改岗位
     *
     * @param jobEntity 岗位信息
     * @return 结果
     */
	public int update(JobEntity jobEntity) {
		FillUserUtil.fillUpdateUserInfo(jobEntity);
	    return jobMapper.update(jobEntity);
	}

	/**
     * 批量删除岗位对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<JobEntity> entities = jobMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "岗位已被删除");

		JobEntity entity = new JobEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return jobMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return jobMapper;
	}

}
