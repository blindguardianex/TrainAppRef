package com.smartru.common.service.jpa;

import com.smartru.common.entity.Task;

import java.util.List;

public interface TaskService {

    Task add(Task task);
    Task update(Task task);
    List<Task> getAllTasksByUser(String login);
}
