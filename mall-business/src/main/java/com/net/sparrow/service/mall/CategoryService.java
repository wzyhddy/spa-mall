package com.net.sparrow.service.mall;

import java.util.List;

import com.net.sparrow.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.mall.CategoryMapper;
import com.net.sparrow.entity.mall.CategoryConditionEntity;
import com.net.sparrow.entity.mall.CategoryEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
/**
 * 分类 服务层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Service
public class CategoryService extends BaseService< CategoryEntity,  CategoryConditionEntity> {

	@Autowired
	private CategoryMapper categoryMapper;

	/**
     * 查询分类信息
     *
     * @param id 分类ID
     * @return 分类信息
     */
	public CategoryEntity findById(Long id) {
	    return categoryMapper.findById(id);
	}

	/**
     * 根据条件分页查询分类列表
     *
     * @param categoryConditionEntity 分类信息
     * @return 分类集合
     */
	public ResponsePageEntity<CategoryEntity> searchByPage(CategoryConditionEntity categoryConditionEntity) {
		int count = categoryMapper.searchCount(categoryConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(categoryConditionEntity);
		}
		List<CategoryEntity> dataList = categoryMapper.searchByCondition(categoryConditionEntity);
		return ResponsePageEntity.build(categoryConditionEntity, count, dataList);
	}

    /**
     * 新增分类
     *
     * @param categoryEntity 分类信息
     * @return 结果
     */
	public int insert(CategoryEntity categoryEntity) {
	    return categoryMapper.insert(categoryEntity);
	}

	/**
     * 修改分类
     *
     * @param categoryEntity 分类信息
     * @return 结果
     */
	public int update(CategoryEntity categoryEntity) {
	    return categoryMapper.update(categoryEntity);
	}

	/**
     * 批量删除分类对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<CategoryEntity> entities = categoryMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "分类已被删除");

		CategoryEntity entity = new CategoryEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return categoryMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return categoryMapper;
	}

}
