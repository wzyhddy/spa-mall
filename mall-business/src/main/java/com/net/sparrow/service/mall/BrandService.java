package com.net.sparrow.service.mall;

import java.util.List;

import com.net.sparrow.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.mall.BrandMapper;
import com.net.sparrow.entity.mall.BrandConditionEntity;
import com.net.sparrow.entity.mall.BrandEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
/**
 * 品牌 服务层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Service
public class BrandService extends BaseService< BrandEntity,  BrandConditionEntity> {

	@Autowired
	private BrandMapper brandMapper;

	/**
     * 查询品牌信息
     *
     * @param id 品牌ID
     * @return 品牌信息
     */
	public BrandEntity findById(Long id) {
	    return brandMapper.findById(id);
	}

	/**
     * 根据条件分页查询品牌列表
     *
     * @param brandConditionEntity 品牌信息
     * @return 品牌集合
     */
	public ResponsePageEntity<BrandEntity> searchByPage(BrandConditionEntity brandConditionEntity) {
		int count = brandMapper.searchCount(brandConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(brandConditionEntity);
		}
		List<BrandEntity> dataList = brandMapper.searchByCondition(brandConditionEntity);
		return ResponsePageEntity.build(brandConditionEntity, count, dataList);
	}

    /**
     * 新增品牌
     *
     * @param brandEntity 品牌信息
     * @return 结果
     */
	public int insert(BrandEntity brandEntity) {
	    return brandMapper.insert(brandEntity);
	}

	/**
     * 修改品牌
     *
     * @param brandEntity 品牌信息
     * @return 结果
     */
	public int update(BrandEntity brandEntity) {
	    return brandMapper.update(brandEntity);
	}

	/**
     * 批量删除品牌对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<BrandEntity> entities = brandMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "品牌已被删除");

		BrandEntity entity = new BrandEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return brandMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return brandMapper;
	}

}
