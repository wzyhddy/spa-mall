package com.net.sparrow.mapper.sys;

import com.net.sparrow.entity.sys.UserRoleConditionEntity;
import com.net.sparrow.entity.sys.UserRoleEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关联 mapper
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
public interface UserRoleMapper extends BaseMapper<UserRoleEntity, UserRoleConditionEntity> {
	/**
     * 查询用户角色关联信息
     *
     * @param id 用户角色关联ID
     * @return 用户角色关联信息
     */
	UserRoleEntity findById(Long id);

	/**
     * 添加用户角色关联
     *
     * @param userRoleEntity 用户角色关联信息
     * @return 结果
     */
	int insert(UserRoleEntity userRoleEntity);

	/**
     * 修改用户角色关联
     *
     * @param userRoleEntity 用户角色关联信息
     * @return 结果
     */
	int update(UserRoleEntity userRoleEntity);

	/**
     * 批量删除用户角色关联
     *
     * @param ids id集合
     * @param entity 用户角色关联实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") UserRoleEntity entity);

	/**
     * 批量查询用户角色关联信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<UserRoleEntity> findByIds(List<Long> ids);

	int batchInsert(List<UserRoleEntity> userRoleEntities);

	void deleteByUserId(Long userId);
}
