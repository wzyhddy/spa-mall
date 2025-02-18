package com.net.sparrow.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.RoleMenuConditionEntity;
import com.net.sparrow.entity.sys.RoleMenuEntity;
import com.net.sparrow.service.sys.RoleMenuService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 角色菜单关联 接口层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Api(tags = "角色菜单关联操作", description = "角色菜单关联接口")
@RestController
@RequestMapping("/v1/roleMenu")
public class RoleMenuController {

	@Autowired
	private RoleMenuService roleMenuService;

	/**
	 * 通过id查询角色菜单关联信息
	 *
	 * @param id 系统ID
	 * @return 角色菜单关联信息
	 */
	@ApiOperation(notes = "通过id查询角色菜单关联信息", value = "通过id查询角色菜单关联信息")
	@GetMapping("/findById")
	public RoleMenuEntity findById(Long id) {
		return roleMenuService.findById(id);
	}

	/**
    * 根据条件查询角色菜单关联列表
    *
    * @param roleMenuConditionEntity 条件
    * @return 角色菜单关联列表
    */
	@ApiOperation(notes = "根据条件查询角色菜单关联列表", value = "根据条件查询角色菜单关联列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<RoleMenuEntity> searchByPage(@RequestBody RoleMenuConditionEntity roleMenuConditionEntity) {
		return roleMenuService.searchByPage(roleMenuConditionEntity);
	}


	/**
     * 添加角色菜单关联
     *
     * @param roleMenuEntity 角色菜单关联实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加角色菜单关联", value = "添加角色菜单关联")
	@PostMapping("/insert")
	public int insert(@RequestBody RoleMenuEntity roleMenuEntity) {
		return roleMenuService.insert(roleMenuEntity);
	}

	/**
     * 修改角色菜单关联
     *
     * @param roleMenuEntity 角色菜单关联实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改角色菜单关联", value = "修改角色菜单关联")
	@PostMapping("/update")
	public int update(@RequestBody RoleMenuEntity roleMenuEntity) {
		return roleMenuService.update(roleMenuEntity);
	}

	/**
     * 批量删除角色菜单关联
     *
     * @param ids 角色菜单关联ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除角色菜单关联", value = "批量删除角色菜单关联")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return roleMenuService.deleteByIds(ids);
	}
}
