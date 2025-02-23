package com.net.sparrow.service.mall;

import java.util.List;

import com.net.sparrow.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.mall.ProductAttributeMapper;
import com.net.sparrow.entity.mall.ProductAttributeConditionEntity;
import com.net.sparrow.entity.mall.ProductAttributeEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
/**
 * 商品属性 服务层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Service
public class ProductAttributeService extends BaseService< ProductAttributeEntity,  ProductAttributeConditionEntity> {

	@Autowired
	private ProductAttributeMapper productAttributeMapper;

	/**
     * 查询商品属性信息
     *
     * @param id 商品属性ID
     * @return 商品属性信息
     */
	public ProductAttributeEntity findById(Long id) {
	    return productAttributeMapper.findById(id);
	}

	/**
     * 根据条件分页查询商品属性列表
     *
     * @param productAttributeConditionEntity 商品属性信息
     * @return 商品属性集合
     */
	public ResponsePageEntity<ProductAttributeEntity> searchByPage(ProductAttributeConditionEntity productAttributeConditionEntity) {
		int count = productAttributeMapper.searchCount(productAttributeConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(productAttributeConditionEntity);
		}
		List<ProductAttributeEntity> dataList = productAttributeMapper.searchByCondition(productAttributeConditionEntity);
		return ResponsePageEntity.build(productAttributeConditionEntity, count, dataList);
	}

    /**
     * 新增商品属性
     *
     * @param productAttributeEntity 商品属性信息
     * @return 结果
     */
	public int insert(ProductAttributeEntity productAttributeEntity) {
	    return productAttributeMapper.insert(productAttributeEntity);
	}

	/**
     * 修改商品属性
     *
     * @param productAttributeEntity 商品属性信息
     * @return 结果
     */
	public int update(ProductAttributeEntity productAttributeEntity) {
	    return productAttributeMapper.update(productAttributeEntity);
	}

	/**
     * 批量删除商品属性对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<ProductAttributeEntity> entities = productAttributeMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "商品属性已被删除");

		ProductAttributeEntity entity = new ProductAttributeEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return productAttributeMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return productAttributeMapper;
	}

}
