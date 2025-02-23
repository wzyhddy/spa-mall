package com.net.sparrow.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.mall.AttributeConditionEntity;
import com.net.sparrow.entity.mall.AttributeEntity;
import com.net.sparrow.service.mall.AttributeService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 属性 接口层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Api(tags = "属性操作", description = "属性接口")
@RestController
@RequestMapping("/v1/attribute")
public class AttributeController {

	@Autowired
	private AttributeService attributeService;

	/**
	 * 通过id查询属性信息
	 *
	 * @param id 系统ID
	 * @return 属性信息
	 */
	@ApiOperation(notes = "通过id查询属性信息", value = "通过id查询属性信息")
	@GetMapping("/findById")
	public AttributeEntity findById(Long id) {
		return attributeService.findById(id);
	}

	/**
    * 根据条件查询属性列表
    *
    * @param attributeConditionEntity 条件
    * @return 属性列表
    */
	@ApiOperation(notes = "根据条件查询属性列表", value = "根据条件查询属性列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<AttributeEntity> searchByPage(@RequestBody AttributeConditionEntity attributeConditionEntity) {
		return attributeService.searchByPage(attributeConditionEntity);
	}


	/**
     * 添加属性
     *
     * @param attributeEntity 属性实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加属性", value = "添加属性")
	@PostMapping("/insert")
	public int insert(@RequestBody AttributeEntity attributeEntity) {
		return attributeService.insert(attributeEntity);
	}

	/**
     * 修改属性
     *
     * @param attributeEntity 属性实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改属性", value = "修改属性")
	@PostMapping("/update")
	public int update(@RequestBody AttributeEntity attributeEntity) {
		return attributeService.update(attributeEntity);
	}

	/**
     * 批量删除属性
     *
     * @param ids 属性ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除属性", value = "批量删除属性")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return attributeService.deleteByIds(ids);
	}
}
