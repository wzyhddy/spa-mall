package com.net.sparrow.mapper.mall;

import com.net.sparrow.entity.mall.ProductEntity;
import com.net.sparrow.entity.mall.ProductPhotoConditionEntity;
import com.net.sparrow.entity.mall.ProductPhotoEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 商品图片 mapper
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
public interface ProductPhotoMapper extends BaseMapper<ProductPhotoEntity, ProductPhotoConditionEntity> {
	/**
     * 查询商品图片信息
     *
     * @param id 商品图片ID
     * @return 商品图片信息
     */
	ProductPhotoEntity findById(Long id);

	/**
     * 添加商品图片
     *
     * @param productPhotoEntity 商品图片信息
     * @return 结果
     */
	int insert(ProductPhotoEntity productPhotoEntity);

	/**
     * 修改商品图片
     *
     * @param productPhotoEntity 商品图片信息
     * @return 结果
     */
	int update(ProductPhotoEntity productPhotoEntity);

	/**
     * 批量删除商品图片
     *
     * @param ids id集合
     * @param entity 商品图片实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductPhotoEntity entity);

	/**
     * 批量查询商品图片信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<ProductPhotoEntity> findByIds(List<Long> ids);

	/**
	 * 批量添加商品图片
	 *
	 * @param productPhotoEntities 商品图片信息
	 * @return 结果
	 */
	int batchInsert(List<ProductPhotoEntity> productPhotoEntities);
}
