package com.smartru.common.service.rabbitmq;


import com.smartru.common.entity.Task;

public interface TaskRabbitService {

    void sendTask(Task task);
}
