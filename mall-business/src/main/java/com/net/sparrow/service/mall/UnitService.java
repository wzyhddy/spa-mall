package com.net.sparrow.service.mall;

import java.util.List;

import com.net.sparrow.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.mall.UnitMapper;
import com.net.sparrow.entity.mall.UnitConditionEntity;
import com.net.sparrow.entity.mall.UnitEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
/**
 * 单位 服务层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Service
public class UnitService extends BaseService< UnitEntity,  UnitConditionEntity> {

	@Autowired
	private UnitMapper unitMapper;

	/**
     * 查询单位信息
     *
     * @param id 单位ID
     * @return 单位信息
     */
	public UnitEntity findById(Long id) {
	    return unitMapper.findById(id);
	}

	/**
     * 根据条件分页查询单位列表
     *
     * @param unitConditionEntity 单位信息
     * @return 单位集合
     */
	public ResponsePageEntity<UnitEntity> searchByPage(UnitConditionEntity unitConditionEntity) {
		int count = unitMapper.searchCount(unitConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(unitConditionEntity);
		}
		List<UnitEntity> dataList = unitMapper.searchByCondition(unitConditionEntity);
		return ResponsePageEntity.build(unitConditionEntity, count, dataList);
	}

    /**
     * 新增单位
     *
     * @param unitEntity 单位信息
     * @return 结果
     */
	public int insert(UnitEntity unitEntity) {
	    return unitMapper.insert(unitEntity);
	}

	/**
     * 修改单位
     *
     * @param unitEntity 单位信息
     * @return 结果
     */
	public int update(UnitEntity unitEntity) {
	    return unitMapper.update(unitEntity);
	}

	/**
     * 批量删除单位对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<UnitEntity> entities = unitMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "单位已被删除");

		UnitEntity entity = new UnitEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return unitMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return unitMapper;
	}

}
