package com.net.sparrow.service.sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.net.sparrow.dto.MenuTreeDTO;
import com.net.sparrow.dto.MetaDTO;
import com.net.sparrow.util.BetweenTimeUtil;
import com.net.sparrow.util.ExcelUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.sys.MenuMapper;
import com.net.sparrow.entity.sys.MenuConditionEntity;
import com.net.sparrow.entity.sys.MenuEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.service.BaseService;

import javax.servlet.http.HttpServletResponse;

/**
 * 菜单 服务层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Service
public class MenuService extends BaseService< MenuEntity,  MenuConditionEntity> {

	@Autowired
	private MenuMapper menuMapper;

	/**
     * 查询菜单信息
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
	public MenuEntity findById(Long id) {
	    return menuMapper.findById(id);
	}

	/**
	 * 根据条件分页查询菜单列表
	 *
	 * @param menuConditionEntity 菜单信息
	 * @return 菜单集合
	 */
	public ResponsePageEntity<MenuEntity> searchByPage(MenuConditionEntity menuConditionEntity) {
		BetweenTimeUtil.parseTime(menuConditionEntity);
		int count = menuMapper.searchCount(menuConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(menuConditionEntity);
		}
		List<MenuEntity> dataList = menuMapper.searchByCondition(menuConditionEntity);
		return ResponsePageEntity.build(menuConditionEntity, count, dataList);
	}

    /**
     * 新增菜单
     *
     * @param menuEntity 菜单信息
     * @return 结果
     */
	public int insert(MenuEntity menuEntity) {
		FillUserUtil.fillCreateUserInfo(menuEntity);
	    return menuMapper.insert(menuEntity);
	}

	/**
     * 修改菜单
     *
     * @param menuEntity 菜单信息
     * @return 结果
     */
	public int update(MenuEntity menuEntity) {
		FillUserUtil.fillUpdateUserInfo(menuEntity);
	    return menuMapper.update(menuEntity);
	}

	/**
     * 批量删除菜单对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<MenuEntity> entities = menuMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "菜单已被删除");

		MenuEntity entity = new MenuEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return menuMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return menuMapper;
	}


	public void export(HttpServletResponse response, MenuConditionEntity menuConditionEntity) throws IOException {
		BetweenTimeUtil.parseTime(menuConditionEntity);
		menuConditionEntity.setPageSize(0);
		List<MenuEntity> menuEntities = menuMapper.searchByCondition(menuConditionEntity);
		ExcelUtil.exportExcel("菜单数据", MenuEntity.class, menuEntities, response);
	}


	public List<MenuTreeDTO> getMenuTree() {
		MenuConditionEntity menuConditionEntity = new MenuConditionEntity();
		menuConditionEntity.setPageSize(0);
		menuConditionEntity.setPid(0L);
		//先查询第一级菜单(父类最顶级菜单)再递归查询子菜单,封装一个菜单树
		List<MenuEntity> menuEntities = menuMapper.searchByCondition(menuConditionEntity);
		if(CollectionUtil.isEmpty(menuEntities)) {
			return Collections.emptyList();
		}
		List<MenuTreeDTO> result = new ArrayList<MenuTreeDTO>();
		for (MenuEntity menuEntity : menuEntities) {
			MenuTreeDTO menuTreeDTO = buildMenuTreeDTO(menuEntity);
			menuTreeDTO.setAlwaysShow(true);
			result.add(menuTreeDTO);
			buildChildren(menuEntity, menuTreeDTO);
		}
		return result;
	}

	private void buildChildren(MenuEntity menuEntity, MenuTreeDTO menuTreeDTO) {
		MenuConditionEntity menuConditionEntity = new MenuConditionEntity();
		menuConditionEntity.setPageSize(0);
		menuConditionEntity.setPid(menuEntity.getId());
		List<MenuEntity> childrenEntities = menuMapper.searchByCondition(menuConditionEntity);
		if (CollectionUtils.isNotEmpty(childrenEntities)) {
			for (MenuEntity childrenEntity : childrenEntities) {
				MenuTreeDTO childMenuTreeDTO = buildMenuTreeDTO(childrenEntity);
				menuTreeDTO.addChildren(childMenuTreeDTO);
				buildChildren(childrenEntity,childMenuTreeDTO);
			}
		}
	}

	private MenuTreeDTO buildMenuTreeDTO(MenuEntity menuEntity) {
		MenuTreeDTO menuTreeDTO = BeanUtil.copyProperties(menuEntity, MenuTreeDTO.class);
		menuTreeDTO.setAlwaysShow(false);
		MetaDTO metaDTO = new MetaDTO();
		menuTreeDTO.setMeta(metaDTO);
		metaDTO.setIcon(menuTreeDTO.getIcon());
		metaDTO.setTitle(menuTreeDTO.getName());
		metaDTO.setNoCache(true);
		return menuTreeDTO;
	}

}
