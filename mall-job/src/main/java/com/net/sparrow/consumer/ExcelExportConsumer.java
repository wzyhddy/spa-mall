package com.net.sparrow.consumer;

import cn.hutool.json.JSONUtil;
import com.net.sparrow.entity.common.CommonNotifyEntity;
import com.net.sparrow.mapper.common.CommonNotifyMapper;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.net.sparrow.config.RabbitConfig.EXCEL_EXPORT_QUEUE;

/**
 * @Author: Sparrow
 * @Description: excel导出消息接收者
 * @DateTime: 2025/5/2 20:10
 **/
@Slf4j
@Component
public class ExcelExportConsumer {
	@Autowired
	private CommonNotifyMapper commonNotifyMapper;

	@RabbitListener(queues = EXCEL_EXPORT_QUEUE)
	public void handler(Message message) {
		byte[] body = message.getBody();
		String content = new String(body);
		log.info("ExcelExportConsumer接收到消息：{}", content);
		CommonNotifyEntity commonTaskEntity = JSONUtil.toBean(content, CommonNotifyEntity.class);
		pushNotify(commonTaskEntity);
	}

	private void pushNotify(CommonNotifyEntity commonNotifyEntity) {
		try {
			WebSocketServer.sendMessage(commonNotifyEntity);
			commonNotifyEntity.setIsPush(1);
			FillUserUtil.mockCurrentUser();
			commonNotifyMapper.update(commonNotifyEntity);
		} catch (IOException e) {
			log.error("WebSocket通知推送失败，原因：", e);
		} finally {
			FillUserUtil.clearCurrentUser();
		}
	}

}
