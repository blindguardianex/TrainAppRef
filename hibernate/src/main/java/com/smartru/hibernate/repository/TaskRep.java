package com.smartru.hibernate.repository;

import com.smartru.common.entity.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRep {

    @Autowired
    private SessionFactory sessionFactory;

    public Task save(Task task){
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;

            transaction = session.beginTransaction();
            session.saveOrUpdate(task);
            transaction.commit();
        }
        return task;
    }

    public Task update(Task task){
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;

            transaction = session.beginTransaction();
            session.update(task);
            transaction.commit();
        }
        return task;
    }
}
