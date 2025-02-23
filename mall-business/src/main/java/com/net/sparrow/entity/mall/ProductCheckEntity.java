package com.net.sparrow.entity.mall;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品检查实体
 */
@ApiModel("商品检查实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCheckEntity {

    /**
     * 分类列表
     */
    private List<CategoryEntity> categoryEntities;

    /**
     * 品牌列表
     */
    private List<BrandEntity> brandEntities;
}
