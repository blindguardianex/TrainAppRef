package com.smartru.consumer.rabbit;

import com.smartru.common.entity.Task;
import com.smartru.common.model.Performer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "task.queue")
public class TaskRabbitListener {

    @Autowired
    @Qualifier("PrimeNumberCheckerPerformer")
    private Performer isPrimeChecker;
    private final String TASK_TYPE = Task.Type.HTTP.toString();

    @RabbitHandler
    public void receiveTask(Task task){
        log.info("Getting task #{} from http", task.getId());
        if (taskHasHttpType(task)) {
            log.info("Getting task #{} from telegram", task.getId());
            isPrimeChecker.perform(task);
        }
        else {
            log.error("Incorrect task type (not http) in http task queue! Task #{}", task.getId());
        }
    }

    private boolean taskHasHttpType(Task task){
        return TASK_TYPE.equals(task.getProperties().get("type").asText());
    }
}
