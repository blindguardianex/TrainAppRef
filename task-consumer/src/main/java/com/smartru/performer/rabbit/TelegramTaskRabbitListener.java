package com.smartru.performer.rabbit;

import com.smartru.common.entity.TelegramTask;
import com.smartru.common.model.TelegramPerformer;
import com.smartru.prime.TaskPerformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "telegram.task.queue")
public class TelegramTaskRabbitListener {

    @Autowired(required = false)
    private TelegramPerformer performer;

    @RabbitHandler
    public void receiveTask(TelegramTask task){
        try {
            System.out.println(task);
            log.info("Getting task #{} from telegram", task.getId());
            performer.perform(task);
        } catch (NullPointerException ex){
            log.error("Application started without telegram module, and task not be perform!");
        }
    }
}
