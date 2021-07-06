package com.smartru.performers;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.service.jpa.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class PerformHelper {

    @Autowired
    private TaskService taskService;

    public TaskResult finishTask(Task task, boolean result){
        TaskResult taskResult = new TaskResult(task, result);
        task.setResult(taskResult);
        taskService.updateResult(task);
        log.info("Task #{} is complete!", task.getId());
        return task.getResult();
    }

    public Optional<Task>findPerformTaskInDatabaseByNum(String num){
        return taskService.getByNum(num);
    }
}
