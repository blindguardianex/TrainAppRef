package com.smartru.receiver.configuration.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private String taskRoutingKey;

    private final AmqpAdmin amqpAdmin;
    private final Exchange taskExchange;
    private final Queue taskQueue;

    @Autowired
    public TaskQueueRabbitStarter(AmqpAdmin amqpAdmin, Exchange taskExchange, Queue taskQueue) {
        this.amqpAdmin = amqpAdmin;
        this.taskExchange = taskExchange;
        this.taskQueue = taskQueue;
    }

    public void declareTaskQueue(){
        Binding binding = BindingBuilder.bind(taskQueue)
                .to(taskExchange)
                .with(taskRoutingKey).noargs();

        amqpAdmin.declareExchange(taskExchange);
        amqpAdmin.declareQueue(taskQueue);
        amqpAdmin.declareBinding(binding);

        log.info("RabbitMQ queue for task is ready!");
    }
}
