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
import com.net.sparrow.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 公共service
 */
@Slf4j
public abstract class BaseService<K, V> {

	public static String TEMP_FILE_PATH = "D:/IdeaProjects/spa_mall/tmp/";

	//分页每页大小
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
	 *
	 * @param v 查询条件
	 * @return 是否自定义
	 */
	public boolean customizeExport(V v) {
		return false;
	}

	/**
	 * 公共excel导出方法
	 *
	 * @param v         查询条件
	 * @param fileName  文件名称
	 * @param clazzName 实体类名称
	 */
	public String export(V v, String fileName, String clazzName) throws ClassNotFoundException {
		if (customizeExport(v)) {
			return null;
		}
		return doExport(v, fileName, clazzName);
	}

	private String doExport(V v, String fileName, String clazzName) throws ClassNotFoundException {
		RequestConditionEntity conditionEntity = (RequestConditionEntity) v;
		BetweenTimeUtil.parseTime(conditionEntity);
		//数据总数量
		int totalCount = getBaseMapper().searchCount(conditionEntity);
		//有多少个sheet页
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
				WriteSheet sheet;
				if (CollectionUtils.isNotEmpty(heads)) {
					sheet = EasyExcel.writerSheet("Sheet" + sheetIndex).head(heads).build();
					List<List<Object>> customizeDataList = handleCustomizeData(dataEntities, heads);
					excelWriter.write(customizeDataList, sheet);
				} else {
					try {
						sheet = EasyExcel.writerSheet("Sheet" + sheetIndex).head(Class.forName(clazzName)).build();
						excelWriter.write(dataEntities, sheet);
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
		return downloadName;
	}

	private List<List<String>> customizeHeader(RequestConditionEntity conditionEntity) {
		List<String> customizeColumnNameList = conditionEntity.getCustomizeColumnNameList();
		if (CollectionUtils.isEmpty(customizeColumnNameList)) {
			return Collections.emptyList();
		}
		return conditionEntity.getCustomizeColumnNameList().stream().map(Lists::newArrayList).collect(Collectors.toList());
	}


	private List<List<Object>> handleCustomizeData(List<K> dataEntities, List<List<String>> heads) {
		if (CollectionUtils.isEmpty(heads)) {
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
					ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
					if (Objects.nonNull(annotation) || ArrayUtil.isEmpty(annotation.value())) {
						fileName = annotation.value()[0];
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
