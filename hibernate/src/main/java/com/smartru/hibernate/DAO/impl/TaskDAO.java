package com.smartru.hibernate.DAO.impl;

import com.smartru.common.entity.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaskDAO extends GenericDAOImpl<Task, Long>{

    @Autowired
    public TaskDAO(SessionFactory sessionFactory) {
        super(sessionFactory, Task.class);
    }
}
