package com.net.sparrow.controller.sys;

import com.net.sparrow.dto.DeptTreeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.DeptConditionEntity;
import com.net.sparrow.entity.sys.DeptEntity;
import com.net.sparrow.service.sys.DeptService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.List;

/**
 * 部门 接口层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:34
 */
@Api(tags = "部门操作", description = "部门接口")
@RestController
@RequestMapping("/v1/dept")
public class DeptController {

	@Autowired
	private DeptService deptService;

	/**
	 * 通过id查询部门信息
	 *
	 * @param id 系统ID
	 * @return 部门信息
	 */
	@ApiOperation(notes = "通过id查询部门信息", value = "通过id查询部门信息")
	@GetMapping("/findById")
	public DeptEntity findById(Long id) {
		return deptService.findById(id);
	}

	/**
    * 根据条件查询部门列表
    *
    * @param deptConditionEntity 条件
    * @return 部门列表
    */
	@ApiOperation(notes = "根据条件查询部门列表", value = "根据条件查询部门列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<DeptTreeDTO> searchByPage(@RequestBody DeptConditionEntity deptConditionEntity) {
		return deptService.searchByPage(deptConditionEntity);
	}


	/**
     * 添加部门
     *
     * @param deptEntity 部门实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加部门", value = "添加部门")
	@PostMapping("/insert")
	public int insert(@RequestBody DeptEntity deptEntity) {
		return deptService.insert(deptEntity);
	}

	/**
     * 修改部门
     *
     * @param deptEntity 部门实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改部门", value = "修改部门")
	@PostMapping("/update")
	public int update(@RequestBody DeptEntity deptEntity) {
		return deptService.update(deptEntity);
	}

	/**
     * 批量删除部门
     *
     * @param ids 部门ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除部门", value = "批量删除部门")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return deptService.deleteByIds(ids);
	}

	/**
	 * 导出部门数据
	 * @return 影响行数
	 */
	@ApiOperation(notes = "导出部门数据", value = "导出部门数据")
	@PostMapping("/export")
	public void export(HttpServletResponse response, DeptConditionEntity deptConditionEntity) throws IOException {
		deptService.export(response, deptConditionEntity);
	}
}
