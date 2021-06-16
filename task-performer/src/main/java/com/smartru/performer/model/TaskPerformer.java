package com.smartru.performer.model;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.service.jpa.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class TaskPerformer {

    private final TaskService taskService;

    @Autowired
    public TaskPerformer(TaskService taskService) {
        this.taskService = taskService;
    }

    public void perform(Task task){
        log.info("Task performer getting task#{}", task.getId());
        String result = performTask(task);
        TaskResult taskResult = new TaskResult(task, result);
        task.setResult(taskResult);
        taskService.update(task);
        log.info("Task #{} is complete!", task.getId());
    }

    private String performTask(Task task){
        return Base64.getEncoder().encodeToString(task.getTaskBody().getBytes(StandardCharsets.UTF_8));
    }
}
