package com.smartru.performer.model;

import com.smartru.common.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "task.queue")
public class TaskRabbitListener {

    @Autowired
    private TaskPerformer performer;

    @RabbitHandler
    public void receiveTask(Task task){
        performer.perform(task);
    }
}
