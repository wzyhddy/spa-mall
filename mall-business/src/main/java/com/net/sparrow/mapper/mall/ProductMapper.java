package com.net.sparrow.mapper.mall;

import com.net.sparrow.entity.mall.ProductConditionEntity;
import com.net.sparrow.entity.mall.ProductEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 商品 mapper
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
public interface ProductMapper extends BaseMapper<ProductEntity, ProductConditionEntity> {
	/**
     * 查询商品信息
     *
     * @param id 商品ID
     * @return 商品信息
     */
	ProductEntity findById(Long id);

	/**
     * 添加商品
     *
     * @param productEntity 商品信息
     * @return 结果
     */
	int insert(ProductEntity productEntity);

	/**
	 * 批量添加商品
	 *
	 * @param productEntities 商品信息
	 * @return 结果
	 */
	int batchInsert(List<ProductEntity> productEntities);


	/**
     * 修改商品
     *
     * @param productEntity 商品信息
     * @return 结果
     */
	int update(ProductEntity productEntity);

	/**
     * 批量删除商品
     *
     * @param ids id集合
     * @param entity 商品实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductEntity entity);

	/**
     * 批量查询商品信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<ProductEntity> findByIds(List<Long> ids);
}
