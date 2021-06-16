package com.smartru.receiver.service.rabbitmq;

import com.smartru.common.entity.Task;
import com.smartru.common.service.rabbitmq.TaskRabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskRabbitServiceImpl implements TaskRabbitService {

    @Autowired
    private RabbitTemplate rabbit;
    @Autowired
    private MessageConverter converter;
    @Autowired
    private Exchange exchange;

    @Override
    public void sendTask(Task task){
        MessageProperties props = new MessageProperties();
        Message message = converter.toMessage(task,props);
        rabbit.send(exchange.getName(),"task.api", message);
        log.info("Task #{} successfully sending", task.getId());
    }
}
