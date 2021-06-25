package com.smartru.performer.model;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.entity.TelegramTask;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.performer.model.checker.DelegatePrimeNumberChecker;
import com.smartru.telegram.PrimeNumberCheckTelegramBot;
import com.smartru.telegram.commands.CheckNumberCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class TaskPerformer {

    private final TaskService taskService;
    private final DelegatePrimeNumberChecker numberChecker;
    private final PrimeNumberCheckTelegramBot bot;

    @Value("${number-checker.digit-capacity.minimum}")
    private int MIN_BOUND_FOR_CHECK_RESULT_IN_DATABASE;

    @Autowired
    public TaskPerformer(TaskService taskService, DelegatePrimeNumberChecker numberChecker, PrimeNumberCheckTelegramBot bot) {
        this.taskService = taskService;
        this.numberChecker = numberChecker;
        this.bot = bot;
    }

    public TaskResult performTask(Task task){
        log.info("Task performer getting task #{} from http", task.getId());
        if(numIsBig(task)){
            Optional<Task> optTask = taskService.getByNum(task.getNum());
            if (optTask.isPresent()){
                return finishTask(task, optTask.get().getResult().isPrime());
            }
        }
        boolean result = numberChecker.isPrimeNumber(task.getNum());
        return finishTask(task, result);
    }

    public void performTelegramTask(TelegramTask task){
        log.info("Task performer getting task #{} from telegram", task.getId());
        task.setResult(performTask(task.getTask()));
        bot.resultReturn(task);
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
