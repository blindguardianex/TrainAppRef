package com.smartru.hibernate.DAO.impl;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.User;
import jdk.swing.interop.SwingInterOpUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
public class UserDAO extends GenericDAOImpl<User, Long>{

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    public Optional<User> findByUsername(String login){
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            Query<User> getByUsernameQuery = session.createQuery("SELECT u FROM User u WHERE u.login = :login");
            getByUsernameQuery.setParameter("login", login);

            transaction = session.beginTransaction();
            User user = getByUsernameQuery.getSingleResult();
            transaction.commit();
            return Optional.of(user);
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }
}
