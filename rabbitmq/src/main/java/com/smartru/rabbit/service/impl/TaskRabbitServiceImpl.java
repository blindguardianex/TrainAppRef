package com.smartru.rabbit.service.impl;

import com.smartru.common.entity.Task;
import com.smartru.rabbit.service.TaskRabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("TaskRabbitBrokerService")
public class TaskRabbitServiceImpl implements TaskRabbitService {

    @Value("${rabbit.task.routing-key}")
    private String httpTaskRoutingKey;

    private final RabbitTemplate rabbit;
    private final MessageConverter converter;
    private final Exchange exchange;

    @Autowired
    public TaskRabbitServiceImpl(RabbitTemplate rabbit, MessageConverter converter, Exchange exchange) {
        this.rabbit = rabbit;
        this.converter = converter;
        this.exchange = exchange;
    }

    @Override
    public void sendTask(Task task){
        MessageProperties props = new MessageProperties();
        Message message = converter.toMessage(task,props);
        rabbit.send(exchange.getName(),httpTaskRoutingKey, message);
        log.info("Http task #{} successfully sending", task.getId());
    }
}
