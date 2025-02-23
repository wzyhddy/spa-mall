package com.net.sparrow.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.mall.UnitConditionEntity;
import com.net.sparrow.entity.mall.UnitEntity;
import com.net.sparrow.service.mall.UnitService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 单位 接口层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Api(tags = "单位操作", description = "单位接口")
@RestController
@RequestMapping("/v1/unit")
public class UnitController {

	@Autowired
	private UnitService unitService;

	/**
	 * 通过id查询单位信息
	 *
	 * @param id 系统ID
	 * @return 单位信息
	 */
	@ApiOperation(notes = "通过id查询单位信息", value = "通过id查询单位信息")
	@GetMapping("/findById")
	public UnitEntity findById(Long id) {
		return unitService.findById(id);
	}

	/**
    * 根据条件查询单位列表
    *
    * @param unitConditionEntity 条件
    * @return 单位列表
    */
	@ApiOperation(notes = "根据条件查询单位列表", value = "根据条件查询单位列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<UnitEntity> searchByPage(@RequestBody UnitConditionEntity unitConditionEntity) {
		return unitService.searchByPage(unitConditionEntity);
	}


	/**
     * 添加单位
     *
     * @param unitEntity 单位实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加单位", value = "添加单位")
	@PostMapping("/insert")
	public int insert(@RequestBody UnitEntity unitEntity) {
		return unitService.insert(unitEntity);
	}

	/**
     * 修改单位
     *
     * @param unitEntity 单位实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改单位", value = "修改单位")
	@PostMapping("/update")
	public int update(@RequestBody UnitEntity unitEntity) {
		return unitService.update(unitEntity);
	}

	/**
     * 批量删除单位
     *
     * @param ids 单位ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除单位", value = "批量删除单位")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return unitService.deleteByIds(ids);
	}
}
