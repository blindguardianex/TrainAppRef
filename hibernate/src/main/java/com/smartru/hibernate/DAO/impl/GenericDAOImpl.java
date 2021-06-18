package com.smartru.hibernate.DAO.impl;

import com.smartru.hibernate.DAO.GenericDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO <T, ID> {

    protected SessionFactory sessionFactory;
    private final Class<T>entityClass;

    public GenericDAOImpl(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    @Override
    public T findByID(ID id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(entityClass, id);
        }
    }

    @Override
    public List<T> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(entityClass);
            query.select(query.from(entityClass));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public Long getCount() {
        try(Session session = sessionFactory.openSession()){
            CriteriaQuery<Long> query = session.getCriteriaBuilder().createQuery(Long.class);
            query.select(session.getCriteriaBuilder().count(query.from(entityClass)));
            return session.createQuery(query).getSingleResult();
        }
    }

    @Override
    public T save(T entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        }
        return entity;
    }

    @Override
    public T update(T entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        }
        return entity;
    }
}
