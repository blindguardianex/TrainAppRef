package com.smartru.common.service.jpa;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task add(Task task);
    Task update(Task task);
    List<Task> getAllTasksByUser(String login);
    Optional<Task> getById(long id);
    void setDeletedStatus(Task task);
    Optional<Task> getByNum(String num);
    void updateResult(Task task);

    default boolean isReady(){
        return true;
    }
}
