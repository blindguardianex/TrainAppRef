package com.smartru.jpa.service;

import com.smartru.common.entity.Task;
import com.smartru.common.exceptions.EntityNotFound;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.jpa.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Qualifier("JpaTaskService")
public class TaskServiceJpaImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task add(Task task) {
        task = taskRepository.saveAndFlush(task);
        log.info("JPA IN add - task: {} successfully saved", task.getTaskBody());
        return task;
    }

    @Override
    public Task update(Task task) {
        Optional<Task> optTask = taskRepository.findById(task.getId());
        if (optTask.isPresent()){
            task=taskRepository.saveAndFlush(task);
            log.info("JPA IN update - task#{} successfully updated in database",task.getId());
            return task;
        }
        log.warn("JPA IN update - task#{} is absent", task.getId());
        throw new EntityNotFound("Task is absent");
    }
}
