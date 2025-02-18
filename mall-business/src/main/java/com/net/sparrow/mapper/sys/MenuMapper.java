package com.net.sparrow.mapper.sys;

import com.net.sparrow.entity.sys.MenuConditionEntity;
import com.net.sparrow.entity.sys.MenuEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单 mapper
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
public interface MenuMapper extends BaseMapper<MenuEntity, MenuConditionEntity> {
	/**
     * 查询菜单信息
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
	MenuEntity findById(Long id);

	/**
     * 添加菜单
     *
     * @param menuEntity 菜单信息
     * @return 结果
     */
	int insert(MenuEntity menuEntity);

	/**
     * 修改菜单
     *
     * @param menuEntity 菜单信息
     * @return 结果
     */
	int update(MenuEntity menuEntity);

	/**
     * 批量删除菜单
     *
     * @param ids id集合
     * @param entity 菜单实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") MenuEntity entity);

	/**
     * 批量查询菜单信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<MenuEntity> findByIds(List<Long> ids);
}
