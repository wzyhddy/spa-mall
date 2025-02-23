package com.net.sparrow.mapper.mall;

import com.net.sparrow.entity.mall.ProductAttributeConditionEntity;
import com.net.sparrow.entity.mall.ProductAttributeEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 商品属性 mapper
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
public interface ProductAttributeMapper extends BaseMapper<ProductAttributeEntity, ProductAttributeConditionEntity> {
	/**
     * 查询商品属性信息
     *
     * @param id 商品属性ID
     * @return 商品属性信息
     */
	ProductAttributeEntity findById(Long id);

	/**
     * 添加商品属性
     *
     * @param productAttributeEntity 商品属性信息
     * @return 结果
     */
	int insert(ProductAttributeEntity productAttributeEntity);

	/**
     * 修改商品属性
     *
     * @param productAttributeEntity 商品属性信息
     * @return 结果
     */
	int update(ProductAttributeEntity productAttributeEntity);

	/**
     * 批量删除商品属性
     *
     * @param ids id集合
     * @param entity 商品属性实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductAttributeEntity entity);

	/**
     * 批量查询商品属性信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<ProductAttributeEntity> findByIds(List<Long> ids);

	/**
	 * 批量添加商品属性
	 *
	 * @param productAttributeEntities 商品属性信息
	 * @return 结果
	 */
	int batchInsert(List<ProductAttributeEntity> productAttributeEntities);

}
