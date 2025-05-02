package com.net.sparrow.service.task;

import cn.hutool.json.JSONUtil;
import com.net.sparrow.annotation.AsyncTask;
import com.net.sparrow.config.RabbitConfig;
import com.net.sparrow.entity.common.CommonNotifyEntity;
import com.net.sparrow.entity.common.CommonTaskEntity;
import com.net.sparrow.enums.ExcelBizTypeEnum;
import com.net.sparrow.enums.TaskStatusEnum;
import com.net.sparrow.enums.TaskTypeEnum;
import com.net.sparrow.exception.BusinessException;
import com.net.sparrow.helper.MqHelper;
import com.net.sparrow.mapper.common.CommonNotifyMapper;
import com.net.sparrow.mapper.common.CommonTaskMapper;
import com.net.sparrow.service.BaseService;
import com.net.sparrow.util.DateFormatUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

import java.util.Date;

import static com.net.sparrow.constant.NumberConstant.NUMBER_3;

/**
 * excel导出服务
 */
@AsyncTask(TaskTypeEnum.EXPORT_EXCEL)
@Slf4j
@Service
public class ExcelExportTask implements IAsyncTask {


	@Autowired
	private CommonTaskMapper commonTaskMapper;
	@Autowired
	private CommonNotifyMapper commonNotifyMapper;
	@Autowired
	private TransactionTemplate transactionTemplate;
	@Autowired
	private MqHelper mqHelper;


	@Override
	public void doTask(CommonTaskEntity commonTaskEntity) {
		doExportExcel(commonTaskEntity);
	}


	private void doExportExcel(CommonTaskEntity commonTaskEntity) {
		Integer bizType = commonTaskEntity.getBizType();
		ExcelBizTypeEnum excelBizTypeEnum = getExcelBizTypeEnum(bizType);
		//任务开始执行时，状态改成执行中
		commonTaskEntity.setStatus(TaskStatusEnum.RUNNING.getValue());
		FillUserUtil.fillUpdateUserInfoFromCreate(commonTaskEntity);
		commonTaskMapper.update(commonTaskEntity);
		try {
			String requestEntity = excelBizTypeEnum.getRequestEntity();
			Class<?> aClass = null;
			try {
				aClass = Class.forName(requestEntity);
			} catch (ClassNotFoundException e) {
				log.error("数据导出异常，没有找到:{}", requestEntity);
				throw new BusinessException(String.format("数据导出异常，没有找到:%s", requestEntity));
			}
			String requestParam = commonTaskEntity.getRequestParam();
			Object toBean = JSONUtil.toBean(requestParam, aClass);
			String serviceName = this.getServiceName(requestEntity);
			BaseService baseService = (BaseService) SpringBeanUtil.getBean(serviceName);
			String fileName = getFileName(excelBizTypeEnum.getDesc());
			String fileUrl = baseService.export(toBean, fileName, this.getEntityName(requestEntity));
			//执行成功
			commonTaskEntity.setFileUrl(fileUrl);
			commonTaskEntity.setStatus(TaskStatusEnum.SUCCESS.getValue());
		} catch (Exception e) {
			log.error("数据导出异常，原因：", e);
			//失败次数加1
			commonTaskEntity.setFailureCount(commonTaskEntity.getFailureCount() + 1);
			//如果失败次数超过3次，则将状态改成失败，后面不再执行
			if (commonTaskEntity.getFailureCount() >= NUMBER_3) {
				commonTaskEntity.setStatus(TaskStatusEnum.FAIL.getValue());
			}
		}

		commonTaskEntity.setUpdateTime(new Date());
		CommonNotifyEntity commonNotifyEntity = transactionTemplate.execute((status) -> {
			commonTaskMapper.update(commonTaskEntity);
			return saveNotifyMessage(commonTaskEntity);
		});
		mqHelper.send(RabbitConfig.EXCEL_EXPORT_EXCHANGE, getRoutingKey(commonTaskEntity.getId()), commonNotifyEntity);


	}

	private ExcelBizTypeEnum getExcelBizTypeEnum(Integer bizType) {
		for (ExcelBizTypeEnum value : ExcelBizTypeEnum.values()) {
			if (value.getValue().equals(bizType)) {
				return value;
			}
		}
		throw new BusinessException("bizType非法");
	}

	private CommonNotifyEntity saveNotifyMessage(CommonTaskEntity commonTaskEntity) {
		CommonNotifyEntity commonNotifyEntity = new CommonNotifyEntity();
		commonNotifyEntity.setTitle("excel导出通知");
		commonNotifyEntity.setContent(getContent(commonTaskEntity));
		commonNotifyEntity.setToUserId(commonTaskEntity.getCreateUserId());
		commonNotifyEntity.setIsPush(0);
		commonNotifyEntity.setType(1);
		commonNotifyEntity.setReadStatus(0);
		commonNotifyEntity.setCreateUserId(commonTaskEntity.getCreateUserId());
		commonNotifyEntity.setCreateUserName(commonTaskEntity.getCreateUserName());
		commonNotifyEntity.setCreateTime(new Date());
		commonNotifyEntity.setIsDel(0);
		commonNotifyMapper.insert(commonNotifyEntity);
		return commonNotifyEntity;
	}

	private String getRoutingKey(Long id) {
		return String.format("excel_export.%s", id);
	}

	private String getContent(CommonTaskEntity commonTaskEntity) {
		StringBuilder contentBuilder = new StringBuilder();
		contentBuilder.append("成功导出excel文件：")
				.append(commonTaskEntity.getName())
				.append("，下载地址：")
				.append(commonTaskEntity.getFileUrl());
		return contentBuilder.toString();
	}

	private String getFileName(String fileName) {
		return String.format("%s数据_%s", fileName, DateFormatUtil.nowDay());
	}


	private String getServiceName(String requestEntity) {
		String[] values = requestEntity.split("\\.");
		if (ObjectUtils.isEmpty(values)) {
			throw new BusinessException(String.format("requestEntity:%s实体格式不正常", requestEntity));
		}
		String entityName = values[values.length - 1];
		String prefix = entityName.substring(0, entityName.indexOf("ConditionEntity"));
		return prefix.substring(0, 1).toLowerCase() + prefix.substring(1) + "Service";
	}

	private String getEntityName(String requestEntity) {
		String[] values = requestEntity.split("\\.");
		if (ObjectUtils.isEmpty(values)) {
			throw new BusinessException(String.format("requestEntity:%s实体格式不正常", requestEntity));
		}
		String entityName = values[values.length - 1];
		entityName = entityName.replace("ConditionEntity", "Entity");

		StringBuilder nameBuilder = new StringBuilder();
		for (int i = 0; i < values.length - 1; i++) {
			nameBuilder.append(values[i]).append(".");
		}
		nameBuilder.append(entityName);
		return nameBuilder.toString();
	}
}
