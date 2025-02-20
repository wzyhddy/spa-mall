//package com.net.sparrow.handler;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.json.JSONUtil;
//import com.net.sparrow.entity.common.CommonNotifyEntity;
//import com.net.sparrow.entity.common.CommonTaskConditionEntity;
//import com.net.sparrow.entity.common.CommonTaskEntity;
//import com.net.sparrow.entity.email.RemoteLoginEmailEntity;
//import com.net.sparrow.enums.ExcelBizTypeEnum;
//import com.net.sparrow.enums.TaskStatusEnum;
//import com.net.sparrow.enums.TaskTypeEnum;
//import com.net.sparrow.exception.BusinessException;
//import com.net.sparrow.mapper.common.CommonNotifyMapper;
//import com.net.sparrow.mapper.common.CommonTaskMapper;
//import com.net.sparrow.service.BaseService;
//import com.net.sparrow.service.email.RemoteLoginEmailService;
//import com.net.sparrow.util.DateFormatUtil;
//import com.net.sparrow.util.FillUserUtil;
//import com.net.sparrow.util.SpringBeanUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.ObjectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.support.TransactionTemplate;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static com.net.sparrow.constant.NumberConstant.NUMBER_3;
//
///**
// * @Author: Sparrow
// * @Description: job
// * @DateTime: 2025/2/18 22:22
// **/
//@Slf4j
//@Component
//public class CommonTaskHandler {
//
//	private static final List<Integer> QUERY_VALID_STATUS_LIST = new ArrayList<>();
//
//	static {
//		QUERY_VALID_STATUS_LIST.add(TaskStatusEnum.WAITING.getValue());
//		QUERY_VALID_STATUS_LIST.add(TaskStatusEnum.RUNNING.getValue());
//	}
//
//	@Autowired
//	private CommonTaskMapper commonTaskMapper;
//
//	@Autowired
//	private TransactionTemplate transactionTemplate;
//
//	@Autowired
//	private CommonNotifyMapper commonNotifyMapper;
//
//	@Autowired
//	private RemoteLoginEmailService remoteLoginEmailService;
//
//	@Scheduled(fixedRate = 1000)
//	public void run() {
//		CommonTaskConditionEntity commonTaskConditionEntity = new CommonTaskConditionEntity();
//		commonTaskConditionEntity.setStatus(TaskStatusEnum.WAITING.getValue());
//		List<CommonTaskEntity> commonTaskEntities = commonTaskMapper.searchByCondition(commonTaskConditionEntity);
//		if(CollectionUtils.isEmpty(commonTaskEntities)){
//			log.info("没有任务需要执行");
//			return;
//		}
//		for (CommonTaskEntity commonTaskEntity : commonTaskEntities) {
//			if (TaskTypeEnum.EXPORT_EXCEL.getValue().equals(commonTaskEntity.getType())) {
//				doExportExcel(commonTaskEntity);
//			} else if (TaskTypeEnum.SEND_EMAIL.getValue().equals(commonTaskEntity.getType())) {
//				doSendEmail(commonTaskEntity);
//			}
//		}
//	}
//
//	private void doExportExcel(CommonTaskEntity commonTaskEntity) {
//		Integer bizType = commonTaskEntity.getBizType();
//		for (ExcelBizTypeEnum value : ExcelBizTypeEnum.values()) {
//			if (value.getValue().equals(bizType)) {
//				commonTaskEntity.setStatus(TaskStatusEnum.RUNNING.getValue());
//				FillUserUtil.fillUpdateUserInfoFromCreate(commonTaskEntity);
//				commonTaskMapper.update(commonTaskEntity);
//				try {
//					String requestEntity = value.getRequestEntity();
//					Class<?> aClass = null;
//					try {
//						aClass = Class.forName(requestEntity);
//					}catch (ClassNotFoundException e){
//						log.error("数据导出异常，没有找到:{}", requestEntity);
//						throw new BusinessException(String.format("数据导出异常，没有找到:%s", requestEntity));
//					}
//
//					String requestParam = commonTaskEntity.getRequestParam();
//					Object toBean = JSONUtil.toBean(requestParam, aClass);
//					String serviceName = this.getServiceName(requestEntity);
//					BaseService baseService = (BaseService) SpringBeanUtil.getBean(serviceName);
//					String fileName = getFileName(value.getDesc());
//					String fileUrl = baseService.export(toBean, fileName, this.getEntityName(requestEntity));
//					commonTaskEntity.setFileUrl(fileUrl);
//					//执行成功
//					commonTaskEntity.setStatus(TaskStatusEnum.SUCCESS.getValue());
//				} catch (Exception e) {
//					log.error("数据导出异常，原因：", e);
//					//失败次数加1
//					commonTaskEntity.setFailureCount(commonTaskEntity.getFailureCount() + 1);
//					//如果失败次数超过3次，则将状态改成失败，后面不再执行
//					if (commonTaskEntity.getFailureCount() >= NUMBER_3) {
//						commonTaskEntity.setStatus(TaskStatusEnum.FAIL.getValue());
//					}
//				}
////				通过 TransactionTemplate，这段代码将 commonTaskMapper.update 和 saveNotifyMessage 两个操作包装在一个事务中
//				commonTaskEntity.setUpdateTime(new Date());
//				transactionTemplate.execute((status) -> {
//					commonTaskMapper.update(commonTaskEntity);
//					saveNotifyMessage(commonTaskEntity);
//					return Boolean.TRUE;
//				});
//				return;
//			}
//		}
//	}
//
//	private void doSendEmail(CommonTaskEntity commonTaskEntity) {
//		//任务开始执行时，状态改成执行中
//		commonTaskEntity.setStatus(TaskStatusEnum.RUNNING.getValue());
//		FillUserUtil.fillUpdateUserInfoFromCreate(commonTaskEntity);
//		commonTaskMapper.update(commonTaskEntity);
//
//		try {
//			RemoteLoginEmailEntity remoteLoginEmailEntity = JSONUtil.toBean(commonTaskEntity.getRequestParam(), RemoteLoginEmailEntity.class);
//			remoteLoginEmailService.sendEmail(remoteLoginEmailEntity);
//			commonTaskEntity.setStatus(TaskStatusEnum.SUCCESS.getValue());
//		} catch (Exception e) {
//			log.error("数据导出异常，原因：", e);
//			//失败次数加1
//			commonTaskEntity.setFailureCount(commonTaskEntity.getFailureCount() + 1);
//			//如果失败次数超过3次，则将状态改成失败，后面不再执行
//			if (commonTaskEntity.getFailureCount() >= NUMBER_3) {
//				commonTaskEntity.setStatus(TaskStatusEnum.FAIL.getValue());
//			}
//		}
//
//		commonTaskEntity.setUpdateTime(new Date());
//		commonTaskMapper.update(commonTaskEntity);
//	}
//	private void saveNotifyMessage(CommonTaskEntity commonTaskEntity) {
//		CommonNotifyEntity commonNotifyEntity = new CommonNotifyEntity();
//		commonNotifyEntity.setTitle("excel导出通知");
//		commonNotifyEntity.setContent(getContent(commonTaskEntity));
//		commonNotifyEntity.setToUserId(commonTaskEntity.getCreateUserId());
//		commonNotifyEntity.setIsPush(0);
//		commonNotifyEntity.setType(1);
//		commonNotifyEntity.setReadStatus(0);
//		commonNotifyEntity.setCreateUserId(commonTaskEntity.getCreateUserId());
//		commonNotifyEntity.setCreateUserName(commonTaskEntity.getCreateUserName());
//		commonNotifyEntity.setCreateTime(new Date());
//		commonNotifyEntity.setIsDel(0);
//		commonNotifyMapper.insert(commonNotifyEntity);
//	}
//
//	private String getContent(CommonTaskEntity commonTaskEntity) {
//		StringBuilder contentBuilder = new StringBuilder();
//		contentBuilder.append("成功导出excel文件：").append(commonTaskEntity.getName()).append("，下载地址：").append(commonTaskEntity.getFileUrl());
//		return contentBuilder.toString();
//	}
//
//	private String getFileName(String fileName) {
//		return fileName + "数据_" + DateFormatUtil.nowDay();
//	}
//
//	private String getServiceName(String requestEntity) {
//		String[] values = requestEntity.split("\\.");
//		if (ObjectUtil.isEmpty(values)) {
//			throw new BusinessException(String.format("requestEntity:%s实体格式不正常", requestEntity));
//		}
//		String entityName = values[values.length - 1];
//		String prefix = entityName.substring(0, entityName.indexOf("ConditionEntity"));
//		return prefix.substring(0, 1).toLowerCase() + prefix.substring(1) + "Service";
//	}
//
//	private String getEntityName(String requestEntity) {
//		String[] values = requestEntity.split("\\.");
//		if (ObjectUtils.isEmpty(values)) {
//			throw new BusinessException(String.format("requestEntity:%s实体格式不正常", requestEntity));
//		}
//		String entityName = values[values.length - 1];
//		entityName = entityName.replace("ConditionEntity", "Entity");
//
//		StringBuilder nameBuilder = new StringBuilder();
//		for (int i = 0; i < values.length - 1; i++) {
//			nameBuilder.append(values[i]).append(".");
//		}
//		nameBuilder.append(entityName);
//		return nameBuilder.toString();
//	}
//}
