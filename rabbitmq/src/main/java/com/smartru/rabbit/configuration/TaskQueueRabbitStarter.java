package com.smartru.rabbit.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Класс, декларирующий очередь сообщений Task перед началом выполнения
 * приложения
 */
@Slf4j
@Service
public class TaskQueueRabbitStarter {

    @Value("${rabbit.task.routing-key}")
    private String httpTaskRoutingKey;
    @Value("${rabbit.task.telegram.routing-key}")
    private String telegramTaskRoutingKey;

    private final AmqpAdmin amqpAdmin;
    private final Exchange taskExchange;
    private final Queue httpTaskQueue;
    private final Queue telegramTaskQueue;

    @Autowired
    public TaskQueueRabbitStarter(AmqpAdmin amqpAdmin,
                                  Exchange taskExchange,
                                  @Qualifier("httpTaskQueue") Queue httpTaskQueue,
                                  @Qualifier("telegramTaskQueue") Queue telegramTaskQueue) {
        this.amqpAdmin = amqpAdmin;
        this.taskExchange = taskExchange;
        this.httpTaskQueue = httpTaskQueue;
        this.telegramTaskQueue = telegramTaskQueue;
    }

    public void declareTaskQueue(){
        Binding httpBinding = BindingBuilder.bind(httpTaskQueue)
                .to(taskExchange)
                .with(httpTaskRoutingKey).noargs();
        Binding telegramBinding = BindingBuilder.bind(telegramTaskQueue)
                .to(taskExchange)
                .with(telegramTaskRoutingKey).noargs();

        amqpAdmin.declareExchange(taskExchange);
        amqpAdmin.declareQueue(httpTaskQueue);
        amqpAdmin.declareBinding(httpBinding);
        amqpAdmin.declareBinding(telegramBinding);

        log.info("RabbitMQ queue for task is ready!");
    }
}
