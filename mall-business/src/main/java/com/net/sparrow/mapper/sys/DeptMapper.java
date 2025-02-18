package com.net.sparrow.mapper.sys;

import com.net.sparrow.entity.sys.DeptConditionEntity;
import com.net.sparrow.entity.sys.DeptEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 部门 mapper
 *
 * @author sparrow
 * @date 2025-02-17 20:14:34
 */
public interface DeptMapper extends BaseMapper<DeptEntity, DeptConditionEntity> {
	/**
     * 查询部门信息
     *
     * @param id 部门ID
     * @return 部门信息
     */
	DeptEntity findById(Long id);

	/**
     * 添加部门
     *
     * @param deptEntity 部门信息
     * @return 结果
     */
	int insert(DeptEntity deptEntity);

	/**
     * 修改部门
     *
     * @param deptEntity 部门信息
     * @return 结果
     */
	int update(DeptEntity deptEntity);

	/**
     * 批量删除部门
     *
     * @param ids id集合
     * @param entity 部门实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") DeptEntity entity);

	/**
     * 批量查询部门信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<DeptEntity> findByIds(List<Long> ids);
}
