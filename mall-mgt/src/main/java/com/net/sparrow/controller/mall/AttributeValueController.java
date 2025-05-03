package com.net.sparrow.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.mall.AttributeValueConditionEntity;
import com.net.sparrow.entity.mall.AttributeValueEntity;
import com.net.sparrow.service.mall.AttributeValueService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 属性值 接口层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Api(tags = "属性值操作", description = "属性值接口")
@RestController
@RequestMapping("/v1/attributeValue")
public class AttributeValueController {

	@Autowired
	private AttributeValueService attributeValueService;

	/**
	 * 通过id查询属性值信息
	 *
	 * @param id 系统ID
	 * @return 属性值信息
	 */
	@ApiOperation(notes = "通过id查询属性值信息", value = "通过id查询属性值信息")
	@GetMapping("/findById")
	public AttributeValueEntity findById(Long id) {
		return attributeValueService.findById(id);
	}

	/**
    * 根据条件查询属性值列表
    *
    * @param attributeValueConditionEntity 条件
    * @return 属性值列表
    */
	@ApiOperation(notes = "根据条件查询属性值列表", value = "根据条件查询属性值列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<AttributeValueEntity> searchByPage(@RequestBody AttributeValueConditionEntity attributeValueConditionEntity) {
		return attributeValueService.searchByPage(attributeValueConditionEntity);
	}


	/**
     * 添加属性值
     *
     * @param attributeValueEntity 属性值实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加属性值", value = "添加属性值")
	@PostMapping("/insert")
	public int insert(@RequestBody AttributeValueEntity attributeValueEntity) {
		return attributeValueService.insert(attributeValueEntity);
	}

	/**
     * 修改属性值
     *
     * @param attributeValueEntity 属性值实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改属性值", value = "修改属性值")
	@PostMapping("/update")
	public int update(@RequestBody AttributeValueEntity attributeValueEntity) {
		return attributeValueService.update(attributeValueEntity);
	}

	/**
     * 批量删除属性值
     *
     * @param ids 属性值ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除属性值", value = "批量删除属性值")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return attributeValueService.deleteByIds(ids);
	}
}
