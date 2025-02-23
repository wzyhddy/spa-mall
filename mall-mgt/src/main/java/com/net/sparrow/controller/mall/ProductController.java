package com.net.sparrow.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.mall.ProductConditionEntity;
import com.net.sparrow.entity.mall.ProductEntity;
import com.net.sparrow.service.mall.ProductService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 商品 接口层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Api(tags = "商品操作", description = "商品接口")
@RestController
@RequestMapping("/v1/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * 通过id查询商品信息
	 *
	 * @param id 系统ID
	 * @return 商品信息
	 */
	@ApiOperation(notes = "通过id查询商品信息", value = "通过id查询商品信息")
	@GetMapping("/findById")
	public ProductEntity findById(Long id) {
		return productService.findById(id);
	}

	/**
    * 根据条件查询商品列表
    *
    * @param productConditionEntity 条件
    * @return 商品列表
    */
	@ApiOperation(notes = "根据条件查询商品列表", value = "根据条件查询商品列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<ProductEntity> searchByPage(@RequestBody ProductConditionEntity productConditionEntity) {
		return productService.searchByPage(productConditionEntity);
	}


	/**
     * 添加商品
     *
     * @param productEntity 商品实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加商品", value = "添加商品")
	@PostMapping("/generate")
	public List<ProductEntity> insert(@RequestBody List<ProductEntity> productEntityList) {
		return productService.generate(productEntityList);
	}

	/**
     * 修改商品
     *
     * @param productEntity 商品实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改商品", value = "修改商品")
	@PostMapping("/update")
	public int update(@RequestBody ProductEntity productEntity) {
		return productService.update(productEntity);
	}

	/**
     * 批量删除商品
     *
     * @param ids 商品ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除商品", value = "批量删除商品")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return productService.deleteByIds(ids);
	}
}
