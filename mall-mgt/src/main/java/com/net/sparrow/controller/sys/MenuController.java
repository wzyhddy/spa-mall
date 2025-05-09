package com.net.sparrow.controller.sys;

import com.net.sparrow.annotation.ExcelExport;
import com.net.sparrow.dto.sys.MenuTreeDTO;
import com.net.sparrow.enums.ExcelBizTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.MenuConditionEntity;
import com.net.sparrow.entity.sys.MenuEntity;
import com.net.sparrow.service.sys.MenuService;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.List;

/**
 * 菜单 接口层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Api(tags = "菜单操作", description = "菜单接口")
@RestController
@RequestMapping("/v1/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	/**
	 * 通过id查询菜单信息
	 *
	 * @param id 系统ID
	 * @return 菜单信息
	 */
	@ApiOperation(notes = "通过id查询菜单信息", value = "通过id查询菜单信息")
	@GetMapping("/findById")
	public MenuEntity findById(Long id) {
		return menuService.findById(id);
	}

	/**
	 * 根据条件查询菜单列表
	 *
	 * @param menuConditionEntity 条件
	 * @return 菜单列表
	 */
	@ApiOperation(notes = "根据条件查询菜单列表", value = "根据条件查询菜单列表")
	@PostMapping("/searchByPage")
	@PreAuthorize("hasRole('admin')")
	public ResponsePageEntity<MenuEntity> searchByPage(@RequestBody MenuConditionEntity menuConditionEntity) {
		return menuService.searchByPage(menuConditionEntity);
	}


	/**
	 * 添加菜单
	 *
	 * @param menuEntity 菜单实体
	 * @return 影响行数
	 */
	@ApiOperation(notes = "添加菜单", value = "添加菜单")
	@PostMapping("/insert")
	public int insert(@RequestBody MenuEntity menuEntity) {
		return menuService.insert(menuEntity);
	}

	/**
	 * 修改菜单
	 *
	 * @param menuEntity 菜单实体
	 * @return 影响行数
	 */
	@ApiOperation(notes = "修改菜单", value = "修改菜单")
	@PostMapping("/update")
	public int update(@RequestBody MenuEntity menuEntity) {
		return menuService.update(menuEntity);
	}

	/**
	 * 批量删除菜单
	 *
	 * @param ids 菜单ID集合
	 * @return 影响行数
	 */
	@ApiOperation(notes = "批量删除菜单", value = "批量删除菜单")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return menuService.deleteByIds(ids);
	}


	@ApiOperation(notes = "获取菜单树", value = "获取菜单树")
	@GetMapping("/getMenuTree")
	public List<MenuTreeDTO> getMenuTree() {
		return menuService.getMenuTree();
	}

	@ApiOperation(notes = "导出菜单数据", value = "导出菜单数据")
	@PostMapping("/export")
	@ExcelExport(ExcelBizTypeEnum.MENU)
	public void export(@RequestBody MenuConditionEntity menuConditionEntity) throws IOException {
	}

	/**
	 * 获取下级菜单
	 *
	 * @return 菜单列表
	 */
	@ApiOperation(notes = "获取下级菜单", value = "获取下级菜单")
	@GetMapping("/getChild")
	public List<Long> getChild(@RequestParam("id") Long id) {
		return menuService.getChild(id);
	}

	/**
	 * 获取逐级加载的菜单
	 *
	 * @return 菜单列表
	 */
	@ApiOperation(notes = "获取逐级加载的菜单", value = "获取逐级加载的菜单")
	@PostMapping("/getMenu")
	public List<MenuTreeDTO> getMenu(@RequestBody MenuConditionEntity menuConditionEntity) {
		return menuService.getMenu(menuConditionEntity);
	}
}
