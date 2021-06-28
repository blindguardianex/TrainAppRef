package com.smartru.common.service.rabbitmq;

import com.smartru.common.entity.TelegramTask;

public interface TelegramTaskRabbitService {

    void sendTelegramTask(TelegramTask task);
}
