package com.net.sparrow.service.mall;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.util.BooleanUtil;
import com.google.common.collect.Lists;
import com.net.sparrow.config.BusinessConfig;
import com.net.sparrow.entity.mall.AttributeConditionEntity;
import com.net.sparrow.entity.mall.AttributeEntity;
import com.net.sparrow.entity.mall.AttributeValueConditionEntity;
import com.net.sparrow.entity.mall.AttributeValueEntity;
import com.net.sparrow.entity.mall.BrandConditionEntity;
import com.net.sparrow.entity.mall.BrandEntity;
import com.net.sparrow.entity.mall.CategoryConditionEntity;
import com.net.sparrow.entity.mall.CategoryEntity;
import com.net.sparrow.entity.mall.ProductAttributeEntity;
import com.net.sparrow.entity.mall.ProductCheckEntity;
import com.net.sparrow.entity.mall.ProductPhotoEntity;
import com.net.sparrow.entity.mall.UnitConditionEntity;
import com.net.sparrow.entity.mall.UnitEntity;
import com.net.sparrow.es.EsTemplate;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.helper.ProductHelper;
import com.net.sparrow.mapper.mall.AttributeMapper;
import com.net.sparrow.mapper.mall.AttributeValueMapper;
import com.net.sparrow.mapper.mall.BrandMapper;
import com.net.sparrow.mapper.mall.CategoryMapper;
import com.net.sparrow.mapper.mall.ProductAttributeMapper;
import com.net.sparrow.mapper.mall.ProductPhotoMapper;
import com.net.sparrow.mapper.mall.UnitMapper;
import com.net.sparrow.service.BaseService;
import com.net.sparrow.util.AttributeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.mall.ProductMapper;
import com.net.sparrow.entity.mall.ProductConditionEntity;
import com.net.sparrow.entity.mall.ProductEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

/**
 * 商品 服务层
 *
 * @author sparrow
 * @date 2025-02-22 19:02:34
 */
@Service
@Slf4j
public class ProductService extends BaseService< ProductEntity,  ProductConditionEntity> {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private BrandMapper brandMapper;
	@Autowired
	private UnitMapper unitMapper;
	@Autowired
	private AttributeMapper attributeMapper;
	@Autowired
	private AttributeValueMapper attributeValueMapper;
	@Autowired
	private ProductPhotoMapper productPhotoMapper;
	@Autowired
	private ProductAttributeMapper productAttributeMapper;
	@Autowired
	private TransactionTemplate transactionTemplate;
	@Autowired
	private ProductHelper productHelper;
	@Autowired
	private EsTemplate esTemplate;
	@Autowired
	private BusinessConfig businessConfig;
	/**
     * 查询商品信息
     *
     * @param id 商品ID
     * @return 商品信息
     */
	public ProductEntity findById(Long id) {
	    return productMapper.findById(id);
	}

	/**
     * 根据条件分页查询商品列表
     *
     * @param productConditionEntity 商品信息
     * @return 商品集合
     */
	public ResponsePageEntity<ProductEntity> searchByPage(ProductConditionEntity productConditionEntity) {
		int count = productMapper.searchCount(productConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(productConditionEntity);
		}
		List<ProductEntity> dataList = productMapper.searchByCondition(productConditionEntity);
		return ResponsePageEntity.build(productConditionEntity, count, dataList);
	}

	/**
	 * 批量新增商品
	 *
	 * @param productEntity 商品信息
	 * @return 结果
	 */
	public List<ProductEntity> generate(List<ProductEntity> productEntityList) {
		final ProductCheckEntity productCheckEntity = new ProductCheckEntity();
		checkParams(productEntityList, productCheckEntity);
		doGenerate(productEntityList, productCheckEntity);
		return productEntityList;
	}

	private void checkParams(List<ProductEntity> productEntityList, ProductCheckEntity productCheckEntity) {
		checkCategory(productEntityList, productCheckEntity);
		checkUnit(productEntityList);
		checkBrand(productEntityList, productCheckEntity);
		checkAttribute(productEntityList);
	}

	private void doGenerate(List<ProductEntity> productEntityList, ProductCheckEntity productCheckEntity) {
		for (ProductEntity productEntity : productEntityList) {
			String modelHash = AttributeUtil.getModelHash(productEntity.getAttributeEntityList());
			productEntity.setHash(modelHash);
			if (!StringUtils.hasLength(productEntity.getModel())) {
				String model = AttributeUtil.getModel(productEntity.getAttributeEntityList());
				productEntity.setModel(model);
			}

			if (!StringUtils.hasLength(productEntity.getName())) {
				String productName = getProductName(productEntity, productCheckEntity);
				productEntity.setName(productName);
			}

			ProductConditionEntity productConditionEntity = new ProductConditionEntity();
			productConditionEntity.setProductEntities(productEntityList);
			List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
			List<ProductEntity> addList = productEntityList.stream().filter(x -> productEntities.stream().noneMatch(p -> getProductKey(p).equals(getProductKey(x))))
					.collect(Collectors.toList());
			this.fillExistId(productEntityList, productEntities);
			if (CollectionUtils.isNotEmpty(addList)) {
				transactionTemplate.execute((status -> {
					productHelper.batchInsert(addList);
					List<ProductAttributeEntity> productAttributeEntities = convertProductAttributeEntityList(productEntityList);
					List<ProductPhotoEntity> productPhotoEntities = convertProductPhotoEntityList(productEntityList);
					if (CollectionUtils.isNotEmpty(productAttributeEntities)) {
						productAttributeMapper.batchInsert(productAttributeEntities);
					}
					if (CollectionUtils.isNotEmpty(productPhotoEntities)) {
						productPhotoMapper.batchInsert(productPhotoEntities);
					}
					return Boolean.TRUE;
				}));
			}
		}
	}


	private String getProductName(ProductEntity productEntity, ProductCheckEntity productCheckEntity) {
		CategoryEntity categoryEntity = productCheckEntity.getCategoryEntities().stream().filter(x -> x.getId().equals(productEntity.getCategoryId()))
				.findAny()
				.orElseThrow(() -> new BusinessException(String.format("分类ID%s在系统中存在", productEntity.getCategoryId())));
		BrandEntity brandEntity = productCheckEntity.getBrandEntities().stream().filter(x -> x.getId().equals(productEntity.getBrandId()))
				.findAny()
				.orElseThrow(() -> new BusinessException(String.format("品牌ID%s在系统中存在", productEntity.getBrandId())));
		return String.format("%s %s", categoryEntity.getName(), brandEntity.getName());
	}


	private void fillExistId(List<ProductEntity> productEntityList, List<ProductEntity> oldProductEntities) {
		for (ProductEntity productEntity : productEntityList) {
			Optional<ProductEntity> productEntityOptional = oldProductEntities.stream()
					.filter(x -> getProductKey(x).equals(getProductKey(productEntity))).findAny();
			if (productEntityOptional.isPresent()) {
				productEntity.setId(productEntityOptional.get().getId());
			}
		}
	}


	private List<ProductAttributeEntity> convertProductAttributeEntityList(List<ProductEntity> productEntityList) {
		List<ProductEntity> filterList = productEntityList.stream().filter(x -> BooleanUtil.isTrue(x.getIsNew())).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(filterList)) {
			return Collections.emptyList();
		}

		List<ProductAttributeEntity> addProductAttributeList = Lists.newArrayList();
		for (ProductEntity productEntity : filterList) {
			List<ProductAttributeEntity> productAttributeEntities = productEntity.getAttributeEntityList().stream().map(x -> {
				ProductAttributeEntity productAttributeEntity = new ProductAttributeEntity();
				productAttributeEntity.setProductId(productEntity.getId());
				productAttributeEntity.setAttributeId(x.getAttributeId());
				productAttributeEntity.setAttributeValueId(x.getId());
				return productAttributeEntity;
			}).collect(Collectors.toList());

			addProductAttributeList.addAll(productAttributeEntities);
		}
		return addProductAttributeList;

	}

	private List<ProductPhotoEntity> convertProductPhotoEntityList(List<ProductEntity> productEntityList) {
		List<ProductEntity> filterList = productEntityList.stream().filter(x -> BooleanUtil.isTrue(x.getIsNew())).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(filterList)) {
			return Collections.emptyList();
		}

		List<ProductPhotoEntity> addProductPhotoList = Lists.newArrayList();
		for (ProductEntity productEntity : filterList) {
			if (CollectionUtils.isNotEmpty(productEntity.getProductPhotoEntityList())) {
				List<ProductPhotoEntity> productPhotoEntities = productEntity.getProductPhotoEntityList().stream().map(x -> {
					ProductPhotoEntity productPhotoEntity = new ProductPhotoEntity();
					productPhotoEntity.setProductId(productEntity.getId());
					productPhotoEntity.setName(x.getName());
					productPhotoEntity.setUrl(x.getUrl());
					productPhotoEntity.setSort(x.getSort());
					return productPhotoEntity;
				}).collect(Collectors.toList());

				addProductPhotoList.addAll(productPhotoEntities);
			}

		}
		return addProductPhotoList;
	}

	private String getProductKey(ProductEntity productEntity) {
		return String.format("%s_%s_%s_%s",
				productEntity.getCategoryId(),
				productEntity.getUnitId(),
				productEntity.getBrandId(),
				productEntity.getHash());
	}

	private void checkCategory(List<ProductEntity> productEntityList, ProductCheckEntity productCheckEntity) {
		CategoryConditionEntity categoryConditionEntity = new CategoryConditionEntity();
		List<Long> categoryIdList = productEntityList.stream().map(ProductEntity::getCategoryId).distinct().collect(Collectors.toList());
		categoryConditionEntity.setIdList(categoryIdList);
		categoryConditionEntity.setPageSize(0);
		List<CategoryEntity> categoryEntities = categoryMapper.searchByCondition(categoryConditionEntity);
		AssertUtil.notEmpty(categoryEntities, "分类不能为空");

		//可以用set优化
		List<Long> notFoundList = categoryIdList.stream().filter(x -> categoryEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
		AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), String.format("分类ID：%s，在系统中不存在", notFoundList));

		productCheckEntity.setCategoryEntities(categoryEntities);
	}

	private void checkUnit(List<ProductEntity> productEntityList) {
		UnitConditionEntity unitConditionEntity = new UnitConditionEntity();
		List<Long> unitIdList = productEntityList.stream().map(ProductEntity::getUnitId).distinct().collect(Collectors.toList());
		unitConditionEntity.setIdList(unitIdList);
		unitConditionEntity.setPageSize(0);
		List<UnitEntity> unitEntities = unitMapper.searchByCondition(unitConditionEntity);
		AssertUtil.notEmpty(unitEntities, "单位不能为空");

		List<Long> notFoundList = unitIdList.stream().filter(x -> unitEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
		AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), String.format("单位ID：%s，在系统中不存在", notFoundList));
	}

	private void checkBrand(List<ProductEntity> productEntityList, ProductCheckEntity productCheckEntity) {
		BrandConditionEntity brandConditionEntity = new BrandConditionEntity();
		List<Long> brandIdList = productEntityList.stream().map(ProductEntity::getBrandId).distinct().collect(Collectors.toList());
		brandConditionEntity.setIdList(brandIdList);
		brandConditionEntity.setPageSize(0);
		List<BrandEntity> brandEntities = brandMapper.searchByCondition(brandConditionEntity);
		AssertUtil.notEmpty(brandEntities, "品牌不能为空");

		List<Long> notFoundList = brandIdList.stream().filter(x -> brandEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
		AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), String.format("品牌ID：%s，在系统中不存在", notFoundList));

		productCheckEntity.setBrandEntities(brandEntities);
	}

	private void checkAttribute(List<ProductEntity> productEntityList) {
		List<AttributeValueEntity> attributeValueEntities = productEntityList.stream().flatMap(x -> x.getAttributeEntityList().stream()).collect(Collectors.toList());

		AttributeValueConditionEntity attributeValueConditionEntity = new AttributeValueConditionEntity();
		List<Long> attributeValueIdList = attributeValueEntities.stream().map(AttributeValueEntity::getId).distinct().collect(Collectors.toList());
		attributeValueConditionEntity.setIdList(attributeValueIdList);
		attributeValueConditionEntity.setPageSize(0);
		List<AttributeValueEntity> attributeValueEntityList = attributeValueMapper.searchByCondition(attributeValueConditionEntity);
		AssertUtil.notEmpty(attributeValueEntityList, "属性值不能为空");

		List<Long> notFoundList = attributeValueIdList.stream().filter(x -> attributeValueEntityList.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
		AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), String.format("属性值ID：%s，在系统中不存在", notFoundList));

		List<Long> attributeIdList = attributeValueEntityList.stream().map(AttributeValueEntity::getAttributeId).collect(Collectors.toList());
		AttributeConditionEntity attributeConditionEntity = new AttributeConditionEntity();
		attributeConditionEntity.setIdList(attributeIdList);
		attributeConditionEntity.setPageSize(0);
		List<AttributeEntity> attributeEntities = attributeMapper.searchByCondition(attributeConditionEntity);
		AssertUtil.notEmpty(attributeEntities, "属性不能为空");

		List<Long> notFoundAttributeList = attributeIdList.stream().filter(x -> attributeEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
		AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundAttributeList), String.format("属性ID：%s，在系统中不存在", notFoundAttributeList));

		Map<Long, List<AttributeValueEntity>> attributeValueMap = attributeValueEntityList.stream().collect(Collectors.groupingBy(AttributeValueEntity::getId));
		Map<Long, List<AttributeEntity>> attributeMap = attributeEntities.stream().collect(Collectors.groupingBy(AttributeEntity::getId));

		for (AttributeValueEntity attributeValueEntity : attributeValueEntities) {
			List<AttributeEntity> subAttributeEntities = attributeMap.get(attributeValueEntity.getAttributeId());
			AssertUtil.notEmpty(subAttributeEntities, String.format("属性ID：%s，在系统中不存在", attributeValueEntity.getAttributeId()));
			attributeValueEntity.setAttributeName(subAttributeEntities.get(0).getName());

			List<AttributeValueEntity> subAttributeValueEntities = attributeValueMap.get(attributeValueEntity.getId());
			AssertUtil.notEmpty(subAttributeValueEntities, String.format("属性值ID：%s，在系统中不存在", attributeValueEntity.getId()));
			attributeValueEntity.setValue(subAttributeValueEntities.get(0).getValue());
		}
	}


	/**
     * 修改商品
     *
     * @param productEntity 商品信息
     * @return 结果
     */
	public int update(ProductEntity productEntity) {
	    return productMapper.update(productEntity);
	}

	/**
     * 批量删除商品对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<ProductEntity> entities = productMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "商品已被删除");

		ProductEntity entity = new ProductEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return productMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return productMapper;
	}

	/**
	 * 根据条件分页搜索商品列表
	 *
	 * @param productConditionEntity 商品信息
	 * @return 商品集合
	 */
	public ResponsePageEntity<ProductEntity> searchFromES(ProductConditionEntity productConditionEntity) {
		try {
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.from(productConditionEntity.getPageBegin());
			searchSourceBuilder.size(productConditionEntity.getPageSize());
			if (StringUtils.hasLength(productConditionEntity.getName())) {
				searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("name", productConditionEntity.getName()));
			}
			if (StringUtils.hasLength(productConditionEntity.getModel())) {
				searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("model", productConditionEntity.getModel()));
			}
			searchSourceBuilder.sort("id", SortOrder.DESC);
			log.info("searchFromES请求参数:", searchSourceBuilder);
			ResponsePageEntity responsePageEntity = ResponsePageEntity.buildEmpty(productConditionEntity);
			List<ProductEntity> productEntities = esTemplate.search(businessConfig.getProductEsIndexName(), searchSourceBuilder, ProductEntity.class, responsePageEntity);
			if (CollectionUtils.isEmpty(productEntities)) {
				return ResponsePageEntity.buildEmpty(productConditionEntity);
			}
			return ResponsePageEntity.build(productConditionEntity, responsePageEntity.getTotalCount(), productEntities);
		} catch (IOException e) {
			log.error("从ES中查询商品失败，原因：", e);
			return ResponsePageEntity.buildEmpty(productConditionEntity);
		}
	}
}
