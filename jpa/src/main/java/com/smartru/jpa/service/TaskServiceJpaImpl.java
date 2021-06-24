package com.smartru.jpa.service;

import com.smartru.common.entity.BaseEntity;
import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.service.jpa.TaskResultService;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.jpa.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Qualifier("JpaTaskService")
public class TaskServiceJpaImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskResultService taskResultService;

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

    @Override
    public Optional<Task> getById(long id) {
        Optional<Task> task = taskRepository.getFullTaskById(id);
        if(task.isPresent()){
            log.info("JPA IN getById - by id #{} find task", id);
        } else {
            log.warn("JPA IN getById - not found task by id #{}", id);
        }
        return task;
    }

    @Override
    @Transactional
    public void setDeletedStatus(Task task) {
        task.setStatus(BaseEntity.Status.DELETED);
        if (task.getResult()!=null){
            task.getResult().setStatus(BaseEntity.Status.DELETED);
        }
        update(task);
        log.info("JPA IN setDeletedStatus - task #{} successfully set deleted status", task.getId());
    }

    @Override
    public Optional<Task> getByNum(String num) {
        Optional<Task>task = taskRepository.findPerformedTaskByNum(num);
        return task;
    }

    @Override
    public void updateResult(Task task) {
        taskResultService.add(task.getResult());
        taskRepository.setResult(task.getResult(),task.getId());
        log.info("JPA IN updateResult - task #{} result successfully updated in database",task.getId());
    }
}
