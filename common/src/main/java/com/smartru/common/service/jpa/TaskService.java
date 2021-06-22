package com.smartru.common.service.jpa;

import com.smartru.common.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task add(Task task);
    Task update(Task task);
    List<Task> getAllTasksByUser(String login);
    Optional<Task> getById(long id);
    void setDeletedStatus(Task task);
}
