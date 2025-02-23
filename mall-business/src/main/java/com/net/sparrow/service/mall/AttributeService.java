package com.net.sparrow.service.mall;

import java.util.List;

import com.net.sparrow.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.mall.AttributeMapper;
import com.net.sparrow.entity.mall.AttributeConditionEntity;
import com.net.sparrow.entity.mall.AttributeEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
/**
 * 属性 服务层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Service
public class AttributeService extends BaseService< AttributeEntity,  AttributeConditionEntity> {

	@Autowired
	private AttributeMapper attributeMapper;

	/**
     * 查询属性信息
     *
     * @param id 属性ID
     * @return 属性信息
     */
	public AttributeEntity findById(Long id) {
	    return attributeMapper.findById(id);
	}

	/**
     * 根据条件分页查询属性列表
     *
     * @param attributeConditionEntity 属性信息
     * @return 属性集合
     */
	public ResponsePageEntity<AttributeEntity> searchByPage(AttributeConditionEntity attributeConditionEntity) {
		int count = attributeMapper.searchCount(attributeConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(attributeConditionEntity);
		}
		List<AttributeEntity> dataList = attributeMapper.searchByCondition(attributeConditionEntity);
		return ResponsePageEntity.build(attributeConditionEntity, count, dataList);
	}

    /**
     * 新增属性
     *
     * @param attributeEntity 属性信息
     * @return 结果
     */
	public int insert(AttributeEntity attributeEntity) {
	    return attributeMapper.insert(attributeEntity);
	}

	/**
     * 修改属性
     *
     * @param attributeEntity 属性信息
     * @return 结果
     */
	public int update(AttributeEntity attributeEntity) {
	    return attributeMapper.update(attributeEntity);
	}

	/**
     * 批量删除属性对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<AttributeEntity> entities = attributeMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "属性已被删除");

		AttributeEntity entity = new AttributeEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return attributeMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return attributeMapper;
	}

}
