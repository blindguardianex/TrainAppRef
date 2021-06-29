package com.smartru.telegram.service;

import com.smartru.common.entity.Task;
import com.smartru.common.service.rabbitmq.TaskRabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("telegramTaskRabbitService")
public class TelegramTaskRabbitServiceImpl implements TaskRabbitService {
    @Value("${rabbit.task.telegram.routing-key}")
    private String telegramTaskRoutingKey;

    private final RabbitTemplate rabbit;
    private final MessageConverter converter;
    private final Exchange exchange;

    @Autowired
    public TelegramTaskRabbitServiceImpl(RabbitTemplate rabbit, MessageConverter converter, Exchange exchange) {
        this.rabbit = rabbit;
        this.converter = converter;
        this.exchange = exchange;
    }

    @Override
    public void sendTask(Task task) {
        MessageProperties props = new MessageProperties();
        Message message = converter.toMessage(task,props);
        rabbit.send(exchange.getName(),telegramTaskRoutingKey, message);
        log.info("Telegram task #{} successfully sending", task.getId());
    }
}
