package com.smartru.hibernate.repository;

import com.smartru.common.entity.TaskResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaskResultRep {

    @Autowired
    private SessionFactory sessionFactory;

    public TaskResult save(TaskResult taskResult){
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;

            transaction = session.beginTransaction();
            session.save(taskResult);
            transaction.commit();
        }
        return taskResult;
    }
}
