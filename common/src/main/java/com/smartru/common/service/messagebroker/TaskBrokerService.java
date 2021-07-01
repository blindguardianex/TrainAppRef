package com.smartru.common.service.messagebroker;

import com.smartru.common.entity.Task;

public interface TaskBrokerService {

    void sendTask(Task task);

    default boolean isReady(){
        return true;
    }
}
