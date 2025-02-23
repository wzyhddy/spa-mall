package com.net.sparrow.service.mall;

import java.util.List;

import com.net.sparrow.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.mall.AttributeValueMapper;
import com.net.sparrow.entity.mall.AttributeValueConditionEntity;
import com.net.sparrow.entity.mall.AttributeValueEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
/**
 * 属性值 服务层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Service
public class AttributeValueService extends BaseService< AttributeValueEntity,  AttributeValueConditionEntity> {

	@Autowired
	private AttributeValueMapper attributeValueMapper;

	/**
     * 查询属性值信息
     *
     * @param id 属性值ID
     * @return 属性值信息
     */
	public AttributeValueEntity findById(Long id) {
	    return attributeValueMapper.findById(id);
	}

	/**
     * 根据条件分页查询属性值列表
     *
     * @param attributeValueConditionEntity 属性值信息
     * @return 属性值集合
     */
	public ResponsePageEntity<AttributeValueEntity> searchByPage(AttributeValueConditionEntity attributeValueConditionEntity) {
		int count = attributeValueMapper.searchCount(attributeValueConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(attributeValueConditionEntity);
		}
		List<AttributeValueEntity> dataList = attributeValueMapper.searchByCondition(attributeValueConditionEntity);
		return ResponsePageEntity.build(attributeValueConditionEntity, count, dataList);
	}

    /**
     * 新增属性值
     *
     * @param attributeValueEntity 属性值信息
     * @return 结果
     */
	public int insert(AttributeValueEntity attributeValueEntity) {
	    return attributeValueMapper.insert(attributeValueEntity);
	}

	/**
     * 修改属性值
     *
     * @param attributeValueEntity 属性值信息
     * @return 结果
     */
	public int update(AttributeValueEntity attributeValueEntity) {
	    return attributeValueMapper.update(attributeValueEntity);
	}

	/**
     * 批量删除属性值对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<AttributeValueEntity> entities = attributeValueMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "属性值已被删除");

		AttributeValueEntity entity = new AttributeValueEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return attributeValueMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return attributeValueMapper;
	}

}
