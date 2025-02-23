package com.net.sparrow.controller.web;

import com.net.sparrow.annotation.NoLogin;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.mall.ProductConditionEntity;
import com.net.sparrow.entity.mall.ProductEntity;
import com.net.sparrow.service.mall.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Sparrow
 * @Description: web端商品操作
 * @DateTime: 2025/2/23 13:38
 **/
@Api(tags = "web商品操作", description = "web商品操作")
@RestController
@RequestMapping("/v1/web/product")
@Validated
public class WebProductController {

	@Autowired
	private ProductService productService;

	/**
	 * 根据条件搜索商品列表
	 *
	 * @param productConditionEntity 条件
	 * @return 商品列表
	 */
	@NoLogin
	@ApiOperation(notes = "根据条件搜索商品列表", value = "根据条件搜索商品列表")
	@PostMapping("/search")
	public ResponsePageEntity<ProductEntity> search(@RequestBody ProductConditionEntity productConditionEntity) {
		return productService.searchFromES(productConditionEntity);
	}
}
