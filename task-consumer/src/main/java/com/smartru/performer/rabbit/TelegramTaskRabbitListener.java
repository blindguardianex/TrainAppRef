package com.smartru.performer.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartru.common.entity.Task;
import com.smartru.common.exceptions.TelegramModuleNotInclude;
import com.smartru.common.model.OuterPerformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "telegram.task.queue")
public class TelegramTaskRabbitListener {

    @Autowired(required = false)
    @Qualifier("telegramPrimeNumberCheckerPerformer")
    private OuterPerformer performer;
    private ObjectMapper mapper = new ObjectMapper();
    private final String TASK_TYPE = "telegram";

    @RabbitHandler
    public void receiveTask(Task task){
        checkTelegramModuleConnection();
        if (taskHasTelegramType(task)) {
            log.info("Getting task #{} from telegram", task.getId());
            performer.perform(task);
        }
        else {
            log.error("Incorrect task type (not telegram)! Task #{}", task.getId());
        }
    }

    private boolean taskHasTelegramType(Task task){
        return TASK_TYPE.equals(task.getProperties().get("type").asText());
    }

    private void checkTelegramModuleConnection(){
        if (performer==null){
            log.error("Please, include telegram module, if you want perform tasks from telegram!");
            throw new TelegramModuleNotInclude();
        }
    }
}
