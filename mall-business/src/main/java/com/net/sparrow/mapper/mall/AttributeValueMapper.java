package com.net.sparrow.mapper.mall;

import com.net.sparrow.entity.mall.AttributeValueConditionEntity;
import com.net.sparrow.entity.mall.AttributeValueEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 属性值 mapper
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
public interface AttributeValueMapper extends BaseMapper<AttributeValueEntity, AttributeValueConditionEntity> {
	/**
     * 查询属性值信息
     *
     * @param id 属性值ID
     * @return 属性值信息
     */
	AttributeValueEntity findById(Long id);

	/**
     * 添加属性值
     *
     * @param attributeValueEntity 属性值信息
     * @return 结果
     */
	int insert(AttributeValueEntity attributeValueEntity);

	/**
     * 修改属性值
     *
     * @param attributeValueEntity 属性值信息
     * @return 结果
     */
	int update(AttributeValueEntity attributeValueEntity);

	/**
     * 批量删除属性值
     *
     * @param ids id集合
     * @param entity 属性值实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") AttributeValueEntity entity);

	/**
     * 批量查询属性值信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<AttributeValueEntity> findByIds(List<Long> ids);
}
