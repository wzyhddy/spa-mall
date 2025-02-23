package com.net.sparrow.mapper.mall;

import com.net.sparrow.entity.mall.BrandConditionEntity;
import com.net.sparrow.entity.mall.BrandEntity;
import com.net.sparrow.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌 mapper
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
public interface BrandMapper extends BaseMapper<BrandEntity, BrandConditionEntity> {
	/**
     * 查询品牌信息
     *
     * @param id 品牌ID
     * @return 品牌信息
     */
	BrandEntity findById(Long id);

	/**
     * 添加品牌
     *
     * @param brandEntity 品牌信息
     * @return 结果
     */
	int insert(BrandEntity brandEntity);

	/**
     * 修改品牌
     *
     * @param brandEntity 品牌信息
     * @return 结果
     */
	int update(BrandEntity brandEntity);

	/**
     * 批量删除品牌
     *
     * @param ids id集合
     * @param entity 品牌实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") BrandEntity entity);

	/**
     * 批量查询品牌信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<BrandEntity> findByIds(List<Long> ids);
}
