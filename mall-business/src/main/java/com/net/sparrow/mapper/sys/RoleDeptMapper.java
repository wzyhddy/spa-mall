package com.net.sparrow.mapper.sys;

import com.net.sparrow.entity.sys.RoleDeptConditionEntity;
import com.net.sparrow.entity.sys.RoleDeptEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 角色部门关联 mapper
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
public interface RoleDeptMapper extends BaseMapper<RoleDeptEntity, RoleDeptConditionEntity> {
	/**
     * 查询角色部门关联信息
     *
     * @param id 角色部门关联ID
     * @return 角色部门关联信息
     */
	RoleDeptEntity findById(Long id);

	/**
     * 添加角色部门关联
     *
     * @param roleDeptEntity 角色部门关联信息
     * @return 结果
     */
	int insert(RoleDeptEntity roleDeptEntity);

	/**
     * 修改角色部门关联
     *
     * @param roleDeptEntity 角色部门关联信息
     * @return 结果
     */
	int update(RoleDeptEntity roleDeptEntity);

	/**
     * 批量删除角色部门关联
     *
     * @param ids id集合
     * @param entity 角色部门关联实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") RoleDeptEntity entity);

	/**
     * 批量查询角色部门关联信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<RoleDeptEntity> findByIds(List<Long> ids);
}
