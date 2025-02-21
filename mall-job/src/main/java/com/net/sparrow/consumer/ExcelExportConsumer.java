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
 * @Description: 消息消费者
 * @DateTime: 2025/2/20 21:58
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
		CommonNotifyEntity commonNotifyEntity = JSONUtil.toBean(content, CommonNotifyEntity.class);
		pushNotify(commonNotifyEntity);
	}

	private void pushNotify(CommonNotifyEntity commonNotifyEntity) {
		try {
			WebSocketServer.sendMessage(commonNotifyEntity);
			commonNotifyEntity.setIsPush(1);
			FillUserUtil.fillUpdateUserInfoFromCreate(commonNotifyEntity);
			commonNotifyMapper.update(commonNotifyEntity);
		} catch (IOException e) {
			log.error("WebSocket通知推送失败，原因：", e);
		}
	}

}
