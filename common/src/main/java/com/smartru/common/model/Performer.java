package com.smartru.common.model;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;

public interface Performer {

    TaskResult perform(Task task);
}
