package com.smartru.hibernate.DAO.impl;

import com.smartru.common.entity.TaskResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaskResultDAO extends GenericDAOImpl<TaskResult,Long>{

    @Autowired
    public TaskResultDAO(SessionFactory sessionFactory) {
        super(sessionFactory, TaskResult.class);
    }
}
