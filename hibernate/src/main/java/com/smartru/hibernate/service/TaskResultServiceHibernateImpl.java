package com.smartru.hibernate.service;

import com.smartru.common.entity.TaskResult;
import com.smartru.common.service.jpa.TaskResultService;
import com.smartru.hibernate.repository.TaskResultRep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("hibernateTaskResultService")
public class TaskResultServiceHibernateImpl implements TaskResultService {

    @Autowired
    private TaskResultRep taskResultRepository;

    @Override
    public TaskResult add(TaskResult result) {
        result = taskResultRepository.save(result);
        log.info("HIBERNATE IN add - task result by task #{} successfully saved", result.getTask().getId());
        return result;
    }
}
