package com.smartru.hibernate.service;

import com.smartru.common.entity.BaseEntity;
import com.smartru.common.entity.Task;
import com.smartru.common.service.jpa.TaskResultService;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.hibernate.DAO.impl.TaskDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Primary
@Qualifier("hibernateTaskService")
public class TaskServiceHibernateImpl implements TaskService {

    @Autowired
    private TaskDAO taskRepository;
    @Autowired
    private TaskResultService taskResultService;

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

    @Override
    public Optional<Task> getById(long id) {
        Optional<Task> task = taskRepository.findFullTaskById(id);
        if(task.isPresent()){
            log.info("HIBERNATE IN getById - by id #{} find task", id);
        } else {
            log.warn("HIBERNATE IN getById - not found task by id #{}", id);
        }
        return task;
    }

    @Override
    public void setDeletedStatus(Task task) {
        task.setStatus(BaseEntity.Status.DELETED);
        if (task.getResult()!=null){
            task.getResult().setStatus(BaseEntity.Status.DELETED);
        }
        update(task);
        log.info("HIBERNATE IN setDeletedStatus - task #{} successfully set deleted status", task.getId());
    }

    @Override
    public Optional<Task> getByNum(String num) {
        return taskRepository.findPerformedTaskByNum(num);
    }

    @Override
    public void updateResult(Task task) {
        taskRepository.setResult(task);
        log.info("HIBERNATE IN updateResult - task #{} result successfully updated in database",task.getId());
    }
}
