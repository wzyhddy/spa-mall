package com.net.sparrow.service.sys;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.net.sparrow.entity.sys.RoleMenuEntity;
import com.net.sparrow.helper.IdGenerateHelper;
import com.net.sparrow.mapper.sys.RoleMenuMapper;
import com.net.sparrow.util.BetweenTimeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.sys.RoleMapper;
import com.net.sparrow.entity.sys.RoleConditionEntity;
import com.net.sparrow.entity.sys.RoleEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色 服务层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Service
public class RoleService extends BaseService< RoleEntity,  RoleConditionEntity> {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Autowired
	private IdGenerateHelper idGenerateHelper;

	/**
     * 查询角色信息
     *
     * @param id 角色ID
     * @return 角色信息
     */
	public RoleEntity findById(Long id) {
	    return roleMapper.findById(id);
	}

	/**
	 * 根据条件分页查询角色列表
	 *
	 * @param roleConditionEntity 角色信息
	 * @return 角色集合
	 */
	public ResponsePageEntity<RoleEntity> searchByPage(RoleConditionEntity roleConditionEntity) {
		BetweenTimeUtil.parseTime(roleConditionEntity);
		int count = roleMapper.searchCount(roleConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(roleConditionEntity);
		}
		List<RoleEntity> dataList = roleMapper.searchByCondition(roleConditionEntity);
		return ResponsePageEntity.build(roleConditionEntity, count, dataList);
	}

	public List<RoleEntity> all() {
		RoleConditionEntity roleConditionEntity = new RoleConditionEntity();
		roleConditionEntity.setPageSize(0);
		return roleMapper.searchByCondition(roleConditionEntity);
	}
    /**
     * 新增角色
     *
     * @param roleEntity 角色信息
     * @return 结果
     */
	public int insert(RoleEntity roleEntity) {
	    return roleMapper.insert(roleEntity);
	}

	/**
     * 修改角色
     *
     * @param roleEntity 角色信息
     * @return 结果
     */
	@Transactional(rollbackFor = Throwable.class)
	public int update(RoleEntity roleEntity) {
		roleMenuMapper.deleteByRoleIds(Lists.newArrayList(roleEntity.getId()));
		saveRoleMenu(roleEntity);
	    return roleMapper.update(roleEntity);
	}

	private void saveRoleMenu(RoleEntity roleEntity) {
		if (CollectionUtils.isEmpty(roleEntity.getMenus())) {
			return;
		}
		List<RoleMenuEntity> roleMenuEntities = roleEntity.getMenus().stream().map(x -> {
			RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
			roleMenuEntity.setId(idGenerateHelper.nextId());
			roleMenuEntity.setRoleId(roleEntity.getId());
			roleMenuEntity.setMenuId(x.getId());
			return roleMenuEntity;
		}).collect(Collectors.toList());
		roleMenuMapper.batchInsert(roleMenuEntities);
	}

	/**
     * 批量删除角色对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<RoleEntity> entities = roleMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "角色已被删除");

		RoleEntity entity = new RoleEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return roleMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return roleMapper;
	}

}
