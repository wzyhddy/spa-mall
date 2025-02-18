package com.net.sparrow.mapper.sys;

import com.net.sparrow.entity.sys.RoleConditionEntity;
import com.net.sparrow.entity.sys.RoleEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 角色 mapper
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
public interface RoleMapper extends BaseMapper<RoleEntity, RoleConditionEntity> {
	/**
     * 查询角色信息
     *
     * @param id 角色ID
     * @return 角色信息
     */
	RoleEntity findById(Long id);

	/**
     * 添加角色
     *
     * @param roleEntity 角色信息
     * @return 结果
     */
	int insert(RoleEntity roleEntity);

	/**
     * 修改角色
     *
     * @param roleEntity 角色信息
     * @return 结果
     */
	int update(RoleEntity roleEntity);

	/**
     * 批量删除角色
     *
     * @param ids id集合
     * @param entity 角色实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") RoleEntity entity);

	/**
     * 批量查询角色信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<RoleEntity> findByIds(List<Long> ids);

	List<RoleEntity> findRoleByUserId(Long id);
}
