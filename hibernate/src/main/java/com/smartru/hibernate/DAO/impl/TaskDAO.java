package com.smartru.hibernate.DAO.impl;

import com.smartru.common.entity.BaseEntity;
import com.smartru.common.entity.Task;
import com.smartru.common.entity.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskDAO extends GenericDAOImpl<Task, Long>{

    @Autowired
    public TaskDAO(SessionFactory sessionFactory) {
        super(sessionFactory, Task.class);
    }

    public List<Task> findByUser(String login){
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Task> criteriaQuery = cb.createQuery(Task.class);
            Root<Task>root=criteriaQuery.from(Task.class);

            List<Predicate>predicates=new ArrayList<>();
            predicates.add(cb.equal(
                    root.get("user").get("login"),
                    cb.parameter(String.class, "userLogin")
            ));
            predicates.add(cb.equal(
                    root.get("status"),
                    cb.parameter(BaseEntity.Status.class, "taskStatus")
            ));

            Query<Task>query=session.createQuery(
                    criteriaQuery.select(root).where(
                        predicates.toArray(new Predicate[]{})
                    )
            ).setParameter("userLogin", login)
                .setParameter("taskStatus", BaseEntity.Status.ACTIVE);

            Transaction transaction = null;
            transaction = session.beginTransaction();
            List<Task>tasks = query.getResultList();
            transaction.commit();
            return tasks;
        }
    }

    public Optional<Task> getFullTaskById(long id){
        try (Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Task> criteriaQuery = cb.createQuery(Task.class);
            Root<Task>root = criteriaQuery.from(Task.class);

            Query<Task>query=session.createQuery(
                    criteriaQuery.select(root).where(
                            cb.equal(
                                    root.get("id"),
                                    cb.parameter(Long.class, "taskId")
                            )
                    )
            ).setParameter("taskId", id);

            Transaction transaction = session.beginTransaction();
            Task task = query.getSingleResult();
            Hibernate.initialize(task.getUser());
            transaction.commit();
            return Optional.of(task);
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }
}
