package com.net.sparrow.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Sparrow
 * @Description: 消息生产者
 * @DateTime: 2025/2/20 20:38
 **/
@Slf4j
@Component
public class MqHelper {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 发送Mq消息
	 * @param routingKey
	 * @param message
	 */
	public void send(String routingKey, Message message) {
		rabbitTemplate.send(routingKey, message);
	}

	public void send(String exchange, String routingKey, Object message) {
		try {
			rabbitTemplate.convertAndSend(exchange, routingKey, message);
			log.info("消息发送成功, exchange:{},routingKey:{},message:{}", exchange, routingKey, message);
		} catch (Exception e) {
			log.error("消息发送失败，原因：", e);
		}
	}


}
