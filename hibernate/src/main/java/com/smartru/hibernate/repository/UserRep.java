package com.smartru.hibernate.repository;

import com.smartru.common.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRep {

    @Autowired
    private SessionFactory sessionFactory;

    public User save(User user){
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;

            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }
        return user;
    }

    public User update(User user){
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;

            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        }
        return user;
    }

    public Optional<User> findByUsername(String login){
        Optional<User>result = null;
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            Query<User> getByUsernameQuery = session.createQuery("SELECT u FROM User u WHERE u.login = :login");
            getByUsernameQuery.setParameter("login", login);

            transaction = session.beginTransaction();
            User user = getByUsernameQuery.getSingleResult();
            transaction.commit();

            if (user!=null) {
                result = Optional.of(user);
            } else {
                result = Optional.empty();
            }
        }
        return result;
    }
}
