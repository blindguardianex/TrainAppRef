package com.smartru.hibernate.DAO.impl;

import com.smartru.common.entity.BaseEntity;
import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.entity.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
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

    public Optional<Task> findFullTaskById(long id){
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

    public Optional<Task> findPerformedTaskByNum(String num){
        try (Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Task> criteriaQuery = cb.createQuery(Task.class);
            Root<Task>root = criteriaQuery.from(Task.class);

            List<Predicate>predicates=new ArrayList<>();
            predicates.add(cb.equal(
                    root.get("num"),
                    cb.parameter(String.class, "taskNum")
            ));
            predicates.add(cb.isNotNull(root.get("result")));


            Query<Task>query=session.createQuery(
                    criteriaQuery.select(root).where(
                            predicates.toArray(new Predicate[]{})
                    )
            ).setParameter("taskNum", num);

            Transaction transaction = session.beginTransaction();
            Task task = query.setMaxResults(1).getSingleResult();
            Hibernate.initialize(task.getUser());
            transaction.commit();
            return Optional.of(task);
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    public void setResult(Task task){
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaUpdate<Task> criteriaUpdate = cb.createCriteriaUpdate(Task.class);
            Root<Task>root = criteriaUpdate.from(Task.class);

            Query setResultQuery = session.createQuery(
                    criteriaUpdate.set("result", task.getResult())
                            .where(cb.equal(
                                    root.get("id"),
                                    cb.parameter(Long.class, "taskId")))
            ).setParameter("taskId", task.getId());

            Transaction transaction = session.beginTransaction();
            session.persist(task.getResult());
            setResultQuery.executeUpdate();
            transaction.commit();
        }
    }
}
