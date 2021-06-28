package com.smartru.prime;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.entity.TelegramTask;
import com.smartru.common.model.Performer;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.prime.checker.DelegatePrimeNumberChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Primary
public class TaskPerformer implements Performer {

    private final TaskService taskService;
    private final DelegatePrimeNumberChecker numberChecker;

    @Value("${number-checker.digit-capacity.minimum:17}")
    private int MIN_BOUND_FOR_CHECK_RESULT_IN_DATABASE;

    @Autowired
    public TaskPerformer(TaskService taskService, DelegatePrimeNumberChecker numberChecker) {
        this.taskService = taskService;
        this.numberChecker = numberChecker;
    }

    @Override
    public TaskResult perform(Task task) {
        if(numIsBig(task)){
            Optional<Task> optTask = taskService.getByNum(task.getNum());
            if (optTask.isPresent()){
                return finishTask(task, optTask.get().getResult().isPrime());
            }
        }
        boolean result = numberChecker.isPrimeNumber(task.getNum());
        return finishTask(task, result);
    }

    private boolean numIsBig(Task task){
        return task.getNum().length() >= MIN_BOUND_FOR_CHECK_RESULT_IN_DATABASE;
    }

    private TaskResult finishTask(Task task, boolean result){
        TaskResult taskResult = new TaskResult(task, result);
        task.setResult(taskResult);
        taskService.updateResult(task);
        log.info("Task #{} is complete!", task.getId());
        return task.getResult();
    }
}
