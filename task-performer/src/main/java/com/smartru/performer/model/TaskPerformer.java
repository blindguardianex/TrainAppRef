package com.smartru.performer.model;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.service.jpa.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskPerformer {

    private final TaskService taskService;
    private final PrimeNumberChecker numberChecker;

    @Autowired
    public TaskPerformer(TaskService taskService, PrimeNumberChecker numberChecker) {
        this.taskService = taskService;
        this.numberChecker = numberChecker;
    }

    public void perform(Task task){
        log.info("Task performer getting task#{}", task.getId());
        Boolean result = numberChecker.isPrimeNumber(task.getNum());
        TaskResult taskResult = new TaskResult(task, result);
        task.setResult(taskResult);
        taskService.update(task);
        log.info("Task #{} is complete!", task.getId());
    }
}
