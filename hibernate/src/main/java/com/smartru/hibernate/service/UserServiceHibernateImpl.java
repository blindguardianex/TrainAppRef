package com.smartru.hibernate.service;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.User;
import com.smartru.common.exceptions.EntityAlreadyExists;
import com.smartru.common.service.jpa.UserService;
import com.smartru.hibernate.DAO.impl.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@Qualifier("hibernateUserService")
public class UserServiceHibernateImpl implements UserService {

    @Autowired
    private UserDAO userRepository;

    @Override
    public User add(User user) throws EntityAlreadyExists {
        Optional<User> optUser = getByUsername(user.getLogin());
        if (optUser.isEmpty()){
            user = userRepository.save(user);
            log.info("HIBERNATE IN add - user: {} successfully saving in database",user.getLogin());
            return user;
        } else {
            log.warn("HIBERNATE IN add - user: {} already exist", user.getLogin());
            throw new EntityAlreadyExists("User already exist: "+user.getLogin());
        }
    }

    @Override
    public Optional<User> getByUsername(String login) {
        Optional<User>optUser =userRepository.findByUsername(login);
        if(optUser.isPresent()){
            log.info("HIBERNATE IN getByUsername - by username: {} find user", login);
        } else {
            log.warn("HIBERNATE IN getByUsername - not found user by username: {}", login);
        }
        return optUser;
    }

    @Override
    public User update(User user) {
        Optional<User>optUser = userRepository.findByUsername(user.getLogin());
        if (optUser.isPresent()){
            user = userRepository.update(user);
            log.info("HIBERNATE IN update - user: {} successfully updated in database",user.getLogin());
            return user;
        }
        log.warn("HIBERNATE IN update - user: {} is absent", user.getLogin());
        throw new EntityNotFoundException("User is absent");
    }

    @Override
    public void updateTokens(User user) {
        userRepository.onlyTokensUpdate(user);
        log.info("HIBERNATE IN updateTokens - user tokens: {} successfully updated in database",user.getLogin());
    }
}
