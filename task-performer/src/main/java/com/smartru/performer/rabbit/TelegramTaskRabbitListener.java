package com.smartru.performer.rabbit;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TelegramTask;
import com.smartru.performer.model.TaskPerformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "telegram.task.queue")
public class TelegramTaskRabbitListener {

    @Autowired
    private TaskPerformer performer;

    @RabbitHandler
    public void receiveTask(TelegramTask task){
        performer.performTelegramTask(task);
    }
}
