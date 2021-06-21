package com.smartru.hibernate.service;

import com.smartru.common.entity.Task;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.hibernate.DAO.impl.TaskDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Primary
@Qualifier("hibernateTaskService")
public class TaskServiceHibernateImpl implements TaskService {

    @Autowired
    private TaskDAO taskRepository;

    @Override
    public Task add(Task task) {
        task = taskRepository.save(task);
        log.info("HIBERNATE IN add - task: {} successfully saved", task.getNum());
        return task;
    }

    @Override
    public Task update(Task task) {
        task = taskRepository.update(task);
        log.info("HIBERNATE IN update - task#{} successfully updated in database",task.getId());
        return task;
    }

    @Override
    public List<Task> getAllTasksByUser(String login) {
        List<Task>tasks = taskRepository.findByUser(login);
        log.info("HIBERNATE IN getAllTasksByUser - by user: {} find {} tasks",login, tasks.size());
        return tasks;
    }
}
