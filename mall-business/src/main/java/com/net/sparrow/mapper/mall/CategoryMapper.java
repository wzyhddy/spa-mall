package com.net.sparrow.mapper.mall;

import com.net.sparrow.entity.mall.CategoryConditionEntity;
import com.net.sparrow.entity.mall.CategoryEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 分类 mapper
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
public interface CategoryMapper extends BaseMapper<CategoryEntity, CategoryConditionEntity> {
	/**
     * 查询分类信息
     *
     * @param id 分类ID
     * @return 分类信息
     */
	CategoryEntity findById(Long id);

	/**
     * 添加分类
     *
     * @param categoryEntity 分类信息
     * @return 结果
     */
	int insert(CategoryEntity categoryEntity);

	/**
     * 修改分类
     *
     * @param categoryEntity 分类信息
     * @return 结果
     */
	int update(CategoryEntity categoryEntity);

	/**
     * 批量删除分类
     *
     * @param ids id集合
     * @param entity 分类实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CategoryEntity entity);

	/**
     * 批量查询分类信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<CategoryEntity> findByIds(List<Long> ids);
}
