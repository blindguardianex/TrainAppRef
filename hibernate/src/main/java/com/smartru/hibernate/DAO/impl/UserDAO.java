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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
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

    public void onlyTokensUpdate(User user){
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaUpdate<User> criteriaUpdate = cb.createCriteriaUpdate(User.class);
            Root<User>root = criteriaUpdate.from(User.class);

            Query query = session.createQuery(
                    criteriaUpdate.set("accessToken", user.getAccessToken())
                                .set("refreshToken",user.getRefreshToken())
                                .where(cb.equal(
                                        root.get("id"),
                                        cb.parameter(Long.class, "userId")))
            ).setParameter("userId", user.getId());

            Transaction transaction = session.beginTransaction();
            query.executeUpdate();
            transaction.commit();
        }
    }
}
