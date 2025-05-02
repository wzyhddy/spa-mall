package com.net.sparrow.service;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import com.net.sparrow.entity.RequestConditionEntity;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.util.BetweenTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.net.sparrow.util.ExcelUtil.TEMP_FILE_PATH;


/**
 * 公共service
 */
@Slf4j
public abstract class BaseService<K, V> {

	@Value("${mall.mgt.exportPageSize:2}")
	private int exportPageSize;

	@Value("${mall.mgt.sheetDataSize:4}")
	private int sheetDataSize;

	/**
	 * 获取BaseMapper
	 *
	 * @return BaseMapper
	 */
	protected abstract BaseMapper getBaseMapper();


	/**
	 * 用户自定义导出逻辑
	 * 是给子类扩展的，给子类一次机会，允许自定义导出数据的逻辑。
	 *
	 * @param v 查询条件
	 * @return 是否自定义
	 */
	public boolean customizeExport(V v) {
		return false;
	}

	public void export(V v, String fileName, String clazzName) {
		if (customizeExport(v)) {
			return;
		}
		doExport(v, fileName, clazzName);
	}

	private void doExport(V v, String fileName, String clazzName) {
		RequestConditionEntity conditionEntity = (RequestConditionEntity) v;
		BetweenTimeUtil.parseTime(conditionEntity);
		int totalCount = getBaseMapper().searchCount(conditionEntity);
		int sheetCount = totalCount % sheetDataSize == 0 ? totalCount / sheetDataSize : totalCount / sheetDataSize + 1;
		//每个sheet页需要循环的次数
		int loopCount = sheetDataSize / exportPageSize;
		String downloadName = TEMP_FILE_PATH + fileName + ".xlsx";
		File file = new File(downloadName);
		List<List<String>> heads = customizeHeader(conditionEntity);
		ExcelWriter excelWriter = EasyExcel.write(file).build();

		conditionEntity.setPageNo(1);
		conditionEntity.setPageSize(exportPageSize);
		for (int sheetIndex = 1; sheetIndex <= sheetCount; sheetIndex++) {
			List<K> dataEntities = getBaseMapper().searchByCondition(conditionEntity);
			int count = 1;
			while (CollectionUtils.isNotEmpty(dataEntities) && count <= loopCount) {
				WriteSheet writeSheet;
				//自定义excel表头
				if (CollectionUtils.isNotEmpty(heads)) {
					writeSheet = EasyExcel.writerSheet("sheet" + sheetIndex).head(heads).build();
					List<List<Object>> customizeDataList = handleCustomizeData(dataEntities, heads);
					excelWriter.write(customizeDataList, writeSheet);
				} else {
					try {
						writeSheet = EasyExcel.writerSheet("sheet" + sheetIndex).head(Class.forName(clazzName)).build();
						excelWriter.write(dataEntities, writeSheet);
					} catch (ClassNotFoundException e) {
						log.error("数据导出异常，没有找到:{}", clazzName);
						throw new BusinessException(String.format("数据导出异常，没有找到:%s", clazzName));
					}
				}

				conditionEntity.setPageNo(conditionEntity.getPageNo() + 1);
				dataEntities = getBaseMapper().searchByCondition(conditionEntity);
				count++;
			}
		}
		excelWriter.finish();
	}

	private List<List<String>> customizeHeader(RequestConditionEntity conditionEntity) {
		List<String> columnNameList = conditionEntity.getCustomizeColumnNameList();
		if (CollectionUtils.isEmpty(columnNameList)) {
			return Collections.emptyList();
		}
		return conditionEntity.getCustomizeColumnNameList().stream().map(x -> Lists.newArrayList(x)).collect(Collectors.toList());
	}

	private List<List<Object>> handleCustomizeData(List<K> dataEntities, List<List<String>> heads) {
		if (CollectionUtils.isEmpty(dataEntities)) {
			return Collections.emptyList();
		}

		List<List<Object>> customizeDataList = Lists.newArrayList();
		for (K dataEntity : dataEntities) {
			List<Object> rowDataList = Lists.newArrayList();
			for (List<String> head : heads) {
				String columnName = head.get(0);
				Field[] fields = dataEntity.getClass().getDeclaredFields();
				for (Field field : fields) {
					String fileName;
					ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
					if (Objects.nonNull(excelProperty) || ArrayUtil.isEmpty(excelProperty.value())) {
						fileName = excelProperty.value()[0];
					} else {
						fileName = field.getName();
					}
					if (!fileName.equals(columnName)) {
						continue;
					}
					field.setAccessible(true);
					try {
						Object fieldValue = field.get(dataEntity);
						rowDataList.add(fieldValue);
					} catch (IllegalAccessException e) {
						log.error("反射获取字段出现异常，原因：", e);
						throw new BusinessException("反射获取字段出现异常");
					}
				}
			}
			customizeDataList.add(rowDataList);
		}
		return customizeDataList;
	}
}
