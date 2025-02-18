package com.net.sparrow.mapper.sys;

import com.net.sparrow.entity.sys.JobConditionEntity;
import com.net.sparrow.entity.sys.JobEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 岗位 mapper
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
public interface JobMapper extends BaseMapper<JobEntity, JobConditionEntity> {
	/**
     * 查询岗位信息
     *
     * @param id 岗位ID
     * @return 岗位信息
     */
	JobEntity findById(Long id);

	/**
     * 添加岗位
     *
     * @param jobEntity 岗位信息
     * @return 结果
     */
	int insert(JobEntity jobEntity);

	/**
     * 修改岗位
     *
     * @param jobEntity 岗位信息
     * @return 结果
     */
	int update(JobEntity jobEntity);

	/**
     * 批量删除岗位
     *
     * @param ids id集合
     * @param entity 岗位实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") JobEntity entity);

	/**
     * 批量查询岗位信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<JobEntity> findByIds(List<Long> ids);
}
