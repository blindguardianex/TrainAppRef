package com.smartru.performer.model;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.service.jpa.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class TaskPerformer {

    private final TaskService taskService;
    private final PrimeNumberChecker numberChecker;

    @Value("${number-checker.digit-capacity.minimum}")
    private int MIN_BOUND_FOR_CHECK_RESULT_IN_DATABASE;

    @Autowired
    public TaskPerformer(TaskService taskService, PrimeNumberChecker numberChecker) {
        this.taskService = taskService;
        this.numberChecker = numberChecker;
    }

    public void perform(Task task){
        log.info("Task performer getting task#{}", task.getId());
        if(findPerformedTaskInDatabase(task)){
            return;
        }
        boolean result = numberChecker.isPrimeNumber(task.getNum());
        finishTask(task, result);
    }

    public boolean findPerformedTaskInDatabase(Task task){
        if(task.getNum().length()< MIN_BOUND_FOR_CHECK_RESULT_IN_DATABASE){
            return false;
        }
        Optional<Task> optTask = taskService.getByNum(task.getNum());
        if (optTask.isEmpty()){
            return false;
        } else {
            finishTask(task, optTask.get().getResult().isPrime());
            return true;
        }
    }

    private void finishTask(Task task, boolean result){
        TaskResult taskResult = new TaskResult(task, result);
        task.setResult(taskResult);
        taskService.updateResult(task);
        log.info("Task #{} is complete!", task.getId());
    }
}
