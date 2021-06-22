package com.smartru.jpa.service;

import com.smartru.common.entity.BaseEntity;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.service.jpa.TaskResultService;
import com.smartru.jpa.repository.TaskResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@Qualifier("JpaTaskResultService")
public class TaskResultServiceJpaImpl implements TaskResultService {

    @Autowired
    private TaskResultRepository resultRepository;

    @Override
    public TaskResult add(TaskResult result) {
        result = resultRepository.saveAndFlush(result);
        log.info("JPA IN add - task result by task #{} successfully saved", result.getTask().getId());
        return result;
    }
}