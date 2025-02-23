package com.net.sparrow.mapper.mall;

import com.net.sparrow.entity.mall.AttributeConditionEntity;
import com.net.sparrow.entity.mall.AttributeEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 属性 mapper
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
public interface AttributeMapper extends BaseMapper<AttributeEntity, AttributeConditionEntity> {
	/**
     * 查询属性信息
     *
     * @param id 属性ID
     * @return 属性信息
     */
	AttributeEntity findById(Long id);

	/**
     * 添加属性
     *
     * @param attributeEntity 属性信息
     * @return 结果
     */
	int insert(AttributeEntity attributeEntity);

	/**
     * 修改属性
     *
     * @param attributeEntity 属性信息
     * @return 结果
     */
	int update(AttributeEntity attributeEntity);

	/**
     * 批量删除属性
     *
     * @param ids id集合
     * @param entity 属性实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") AttributeEntity entity);

	/**
     * 批量查询属性信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<AttributeEntity> findByIds(List<Long> ids);
}
