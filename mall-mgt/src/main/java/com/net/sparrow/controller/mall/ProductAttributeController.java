package com.net.sparrow.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.mall.ProductAttributeConditionEntity;
import com.net.sparrow.entity.mall.ProductAttributeEntity;
import com.net.sparrow.service.mall.ProductAttributeService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 商品属性 接口层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Api(tags = "商品属性操作", description = "商品属性接口")
@RestController
@RequestMapping("/v1/productAttribute")
public class ProductAttributeController {

	@Autowired
	private ProductAttributeService productAttributeService;

	/**
	 * 通过id查询商品属性信息
	 *
	 * @param id 系统ID
	 * @return 商品属性信息
	 */
	@ApiOperation(notes = "通过id查询商品属性信息", value = "通过id查询商品属性信息")
	@GetMapping("/findById")
	public ProductAttributeEntity findById(Long id) {
		return productAttributeService.findById(id);
	}

	/**
    * 根据条件查询商品属性列表
    *
    * @param productAttributeConditionEntity 条件
    * @return 商品属性列表
    */
	@ApiOperation(notes = "根据条件查询商品属性列表", value = "根据条件查询商品属性列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<ProductAttributeEntity> searchByPage(@RequestBody ProductAttributeConditionEntity productAttributeConditionEntity) {
		return productAttributeService.searchByPage(productAttributeConditionEntity);
	}


	/**
     * 添加商品属性
     *
     * @param productAttributeEntity 商品属性实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加商品属性", value = "添加商品属性")
	@PostMapping("/insert")
	public int insert(@RequestBody ProductAttributeEntity productAttributeEntity) {
		return productAttributeService.insert(productAttributeEntity);
	}

	/**
     * 修改商品属性
     *
     * @param productAttributeEntity 商品属性实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改商品属性", value = "修改商品属性")
	@PostMapping("/update")
	public int update(@RequestBody ProductAttributeEntity productAttributeEntity) {
		return productAttributeService.update(productAttributeEntity);
	}

	/**
     * 批量删除商品属性
     *
     * @param ids 商品属性ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除商品属性", value = "批量删除商品属性")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return productAttributeService.deleteByIds(ids);
	}
}
