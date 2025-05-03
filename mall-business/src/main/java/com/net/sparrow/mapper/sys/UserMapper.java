package com.net.sparrow.mapper.sys;

import com.net.sparrow.entity.sys.UserConditionEntity;
import com.net.sparrow.entity.sys.UserEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 mapper
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
public interface UserMapper extends BaseMapper<UserEntity, UserConditionEntity> {
	/**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
	UserEntity findById(Long id);

	/**
     * 添加用户
     *
     * @param userEntity 用户信息
     * @return 结果
     */
	int insert(UserEntity userEntity);

	/**
     * 修改用户
     *
     * @param userEntity 用户信息
     * @return 结果
     */
	int update(UserEntity userEntity);

	/**
     * 批量删除用户
     *
     * @param ids id集合
     * @param entity 用户实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") UserEntity entity);

	/**
     * 批量查询用户信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<UserEntity> findByIds(List<Long> ids);

	UserEntity findByUserName(String username);

	int updateForBatch(List<UserEntity> list);
}
