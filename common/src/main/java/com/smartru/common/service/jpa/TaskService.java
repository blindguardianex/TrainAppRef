package com.smartru.common.service.jpa;

import com.smartru.common.entity.Task;

public interface TaskService {

    Task add(Task task);
    Task update(Task task);
}
