package com.net.sparrow.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitConfig {

    /**
     * excel导出交换机
     */
    public static final String EXCEL_EXPORT_EXCHANGE = "excel_export_exchange";
    /**
     * excel导出队列
     */
    public static final String EXCEL_EXPORT_QUEUE = "excel_export_queue";
    /**
     * excel导出路由key
     */
    public static final String EXCEL_EXPORT_QUEUE_ROUTING_KEY = "excel_export.#";

    /**
     * 延迟时间 （单位：ms(毫秒)）
     */
    public static final Integer DELAY_TIME = 10000;

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }


    /**
     * 创建excel导出交换机
     */
    @Bean("excelExportExchange")
    public Exchange excelExportExchange() {
        return new TopicExchange(EXCEL_EXPORT_EXCHANGE, true, false);
    }


    /**
     * 创建excel导出队列
     */
    @Bean("excelExportQueue")
    public Queue excelExportQueue() {
        Map<String, Object> args = new HashMap<>(1);
        //过期时间，单位毫秒
        args.put("x-message-ttl", DELAY_TIME);
        return QueueBuilder.durable(EXCEL_EXPORT_QUEUE).withArguments(args).build();
    }

    /**
     * 绑定excel导出交换机和队列
     */
    @Bean("excelExportBinding")
    public Binding excelExportBinding(@Qualifier("excelExportQueue") Queue queue,
                                      @Qualifier("excelExportExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EXCEL_EXPORT_QUEUE_ROUTING_KEY).noargs();
    }


    @Bean
    public MessageConverter jsonToMapMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}