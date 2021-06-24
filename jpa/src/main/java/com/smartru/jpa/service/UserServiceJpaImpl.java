package com.smartru.jpa.service;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.User;
import com.smartru.common.exceptions.EntityAlreadyExists;
import com.smartru.common.service.jpa.UserService;
import com.smartru.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Qualifier("JpaUserService")
public class UserServiceJpaImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User add(User user) throws EntityAlreadyExists {
        Optional<User>optUser = repository.findByUsername(user.getLogin());
        if (optUser.isEmpty()){
            user = repository.saveAndFlush(user);
            log.info("JPA IN add - user: {} successfully saving in database",user.getLogin());
            return user;
        }
        log.warn("JPA IN add - user: {} already exist", user.getLogin());
        throw new EntityAlreadyExists("User already exist");
    }

    @Override
    public Optional<User> getByUsername(String login) {
        Optional<User>optUser =repository.findByUsername(login);
        if(optUser.isPresent()){
            log.info("JPA IN getByUsername - by username: {} find user", login);
        } else {
            log.warn("JPA IN getByUsername - not found user by username: {}", login);
        }
        return optUser;
    }

    @Override
    public User update(User user) {
        if (repository.existsById(user.getId())){
            user = repository.saveAndFlush(user);
            log.info("JPA IN update - user: {} successfully updated in database",user.getLogin());
            return user;
        }
        log.warn("JPA IN update - user: {} is absent", user.getLogin());
        throw new EntityNotFoundException("User is absent");
    }

    @Override
    public void updateTokens(User user) {
        repository.onlyTokensUpdate(user.getId(),
                                        user.getAccessToken(),
                                        user.getRefreshToken());
        log.info("JPA IN updateTokens - user tokens: {} successfully updated in database",user.getLogin());
    }
}
