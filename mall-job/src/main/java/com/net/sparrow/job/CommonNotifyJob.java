//package com.net.sparrow.job;
//
//import com.net.sparrow.entity.common.CommonNotifyConditionEntity;
//import com.net.sparrow.entity.common.CommonNotifyEntity;
//import com.net.sparrow.mapper.common.CommonNotifyMapper;
//import com.net.sparrow.util.FillUserUtil;
//import com.net.sparrow.websocket.WebSocketServer;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * @Author: Sparrow
// * @Description: CommonNotifyJob
// * @DateTime: 2025/2/19 16:55
// **/
//@Slf4j
//@Component
//public class CommonNotifyJob {
//
//	@Autowired
//	private CommonNotifyMapper commonNotifyMapper;
//
//	@Scheduled(fixedRate = 1000)
//	public void run() {
//		CommonNotifyConditionEntity conditionEntity = new CommonNotifyConditionEntity();
//		conditionEntity.setIsPush(0);
//		conditionEntity.setIsDel(0);
//		List<CommonNotifyEntity> commonNotifyEntities = commonNotifyMapper.searchByCondition(conditionEntity);
//		if (CollectionUtils.isEmpty(commonNotifyEntities)) {
//			log.info("==没有通知需要推送==");
//			return;
//		}
//		for (CommonNotifyEntity commonNotifyEntity : commonNotifyEntities) {
//			pushNotify(commonNotifyEntity);
//		}
//	}
//
//	private void pushNotify(CommonNotifyEntity commonNotifyEntity) {
//		try {
//			WebSocketServer.sendMessage(commonNotifyEntity);
//			commonNotifyEntity.setIsPush(1);
//			FillUserUtil.fillUpdateUserInfoFromCreate(commonNotifyEntity);
//			commonNotifyMapper.update(commonNotifyEntity);
//		} catch (IOException e) {
//			log.error("WebSocket通知推送失败，原因：", e);
//		}
//
//	}
//}
