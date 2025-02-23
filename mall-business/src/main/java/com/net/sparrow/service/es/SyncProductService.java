package com.net.sparrow.service.es;

import com.net.sparrow.config.BusinessConfig;
import com.net.sparrow.constant.NumberConstant;
import com.net.sparrow.entity.EsBaseEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.mall.ProductConditionEntity;
import com.net.sparrow.entity.mall.ProductEntity;
import com.net.sparrow.es.EsTemplate;
import com.net.sparrow.service.mall.ProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 同步ES商品service
 */
@Service
public class SyncProductService {
    @Autowired
    private ProductService productService;
    @Autowired
    private EsTemplate esTemplate;

    @Autowired
    private BusinessConfig businessConfig;

    /**
     * 同步商品到ES
     */
    public void syncProductToES() {
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setPageSize(NumberConstant.NUMBER_500);
        ResponsePageEntity<ProductEntity> productEntityResponsePageEntity = productService.searchByPage(productConditionEntity);

        while (CollectionUtils.isNotEmpty(productEntityResponsePageEntity.getData())) {
            saveData(productEntityResponsePageEntity.getData());

            productConditionEntity.setPageNo(productConditionEntity.getPageNo() + 1);
            productEntityResponsePageEntity = productService.searchByPage(productConditionEntity);
        }
    }


    private void saveData(List<ProductEntity> productEntities) {
        List<EsBaseEntity> dataList = productEntities.stream().map(x -> {
            EsBaseEntity esBaseEntity = new EsBaseEntity();
            esBaseEntity.setId(x.getId().toString());
            Map<String, Object> map = new HashMap<>();
            map.put("id", x.getId());
            map.put("name", x.getName());
            map.put("model", x.getModel());
            map.put("quantity", x.getQuantity());
            map.put("price", x.getPrice());
            esBaseEntity.setData(map);
            return esBaseEntity;
        }).collect(Collectors.toList());

        esTemplate.batchInsert(businessConfig.getProductEsIndexName(), dataList);
    }
}