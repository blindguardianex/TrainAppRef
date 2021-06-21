package com.smartru.jpa.service;

import com.smartru.common.entity.Task;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.jpa.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
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
        log.info("JPA IN add - task: {} successfully saved", task.getNum());
        return task;
    }

    @Override
    public Task update(Task task) {
        if (taskRepository.existsById(task.getId())){
            task=taskRepository.saveAndFlush(task);
            log.info("JPA IN update - task#{} successfully updated in database",task.getId());
            return task;
        }
        log.warn("JPA IN update - task#{} is absent", task.getId());
        throw new EntityNotFoundException("Task is absent");
    }

    @Override
    public List<Task> getAllTasksByUser(String login) {
        List<Task>tasks = taskRepository.findByUser(login);
        log.info("JPA IN getAllTasksByUser - by user: {} find {} tasks",login, tasks.size());
        return tasks;
    }
}
