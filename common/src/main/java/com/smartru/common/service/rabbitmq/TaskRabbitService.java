package com.smartru.common.service.rabbitmq;


import com.smartru.common.entity.Task;
import com.smartru.common.entity.TelegramTask;

public interface TaskRabbitService {

    void sendTask(Task task);
    void sendTelegramTask(TelegramTask task);
}
