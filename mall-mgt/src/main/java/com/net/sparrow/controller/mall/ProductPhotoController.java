package com.net.sparrow.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.mall.ProductPhotoConditionEntity;
import com.net.sparrow.entity.mall.ProductPhotoEntity;
import com.net.sparrow.service.mall.ProductPhotoService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

/**
 * 商品图片 接口层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Api(tags = "商品图片操作", description = "商品图片接口")
@RestController
@RequestMapping("/v1/productPhoto")
public class ProductPhotoController {

	@Autowired
	private ProductPhotoService productPhotoService;

	/**
	 * 通过id查询商品图片信息
	 *
	 * @param id 系统ID
	 * @return 商品图片信息
	 */
	@ApiOperation(notes = "通过id查询商品图片信息", value = "通过id查询商品图片信息")
	@GetMapping("/findById")
	public ProductPhotoEntity findById(Long id) {
		return productPhotoService.findById(id);
	}

	/**
    * 根据条件查询商品图片列表
    *
    * @param productPhotoConditionEntity 条件
    * @return 商品图片列表
    */
	@ApiOperation(notes = "根据条件查询商品图片列表", value = "根据条件查询商品图片列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<ProductPhotoEntity> searchByPage(@RequestBody ProductPhotoConditionEntity productPhotoConditionEntity) {
		return productPhotoService.searchByPage(productPhotoConditionEntity);
	}


	/**
     * 添加商品图片
     *
     * @param productPhotoEntity 商品图片实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加商品图片", value = "添加商品图片")
	@PostMapping("/insert")
	public int insert(@RequestBody ProductPhotoEntity productPhotoEntity) {
		return productPhotoService.insert(productPhotoEntity);
	}

	/**
     * 修改商品图片
     *
     * @param productPhotoEntity 商品图片实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改商品图片", value = "修改商品图片")
	@PostMapping("/update")
	public int update(@RequestBody ProductPhotoEntity productPhotoEntity) {
		return productPhotoService.update(productPhotoEntity);
	}

	/**
     * 批量删除商品图片
     *
     * @param ids 商品图片ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除商品图片", value = "批量删除商品图片")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return productPhotoService.deleteByIds(ids);
	}
}
