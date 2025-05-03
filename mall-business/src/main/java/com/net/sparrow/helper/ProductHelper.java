package com.net.sparrow.helper;

import com.net.sparrow.entity.mall.ProductConditionEntity;
import com.net.sparrow.entity.mall.ProductEntity;
import com.net.sparrow.mapper.mall.ProductMapper;
import com.net.sparrow.util.AssertUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import java.util.List;
import java.util.Objects;

/**
 * 商品帮助类
 */
@Component
public class ProductHelper {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private IdGenerateHelper idGenerateHelper;

    /**
     * 批量insert商品
     *
     * @param productEntities 商品列表
     */
    public void batchInsert(List<ProductEntity> productEntities) {
        AssertUtil.notEmpty(productEntities, "商品数据不能为空");

        for (ProductEntity productEntity : productEntities) {
            doInsert(productEntity);
        }
    }

    private void doInsert(ProductEntity productEntity) {
        ProductEntity oldProductEntity = queryOldProductEntity(productEntity);
        if (Objects.nonNull(oldProductEntity)) {
            productEntity.setId(oldProductEntity.getId());
            return;
        }

        try {
            productEntity.setId(idGenerateHelper.nextId());
            productMapper.batchInsert(Lists.newArrayList(productEntity));
            productEntity.setIsNew(true);
            //如果抛了DuplicateKeyException异常，说明此时有另外的请求并发insert商品数据成功了
        } catch (DuplicateKeyException e) {
            oldProductEntity = queryOldProductEntity(productEntity);
            productEntity.setId(oldProductEntity.getId());
        }
    }

    private ProductEntity queryOldProductEntity(ProductEntity productEntity) {
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setCategoryId(productEntity.getCategoryId());
        productConditionEntity.setUnitId(productEntity.getUnitId());
        productConditionEntity.setBrandId(productEntity.getBrandId());
        productConditionEntity.setHash(productEntity.getHash());
        productConditionEntity.setPageSize(1);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        if (CollectionUtils.isNotEmpty(productEntities)) {
            return productEntities.get(0);
        }
        return null;
    }
}