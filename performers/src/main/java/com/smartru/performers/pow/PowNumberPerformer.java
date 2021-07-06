package com.smartru.performers.pow;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.model.Performer;
import com.smartru.performers.PerformHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PowNumberPerformer implements Performer {

    @Autowired
    private PerformHelper helper;
    @Override
    public TaskResult perform(Task task) {
        return null;
    }
}
