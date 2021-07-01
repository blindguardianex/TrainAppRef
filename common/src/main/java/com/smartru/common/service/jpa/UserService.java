package com.smartru.common.service.jpa;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.User;
import com.smartru.common.exceptions.EntityAlreadyExists;

import java.util.Optional;

public interface UserService {

    User add(User user) throws EntityAlreadyExists;
    Optional<User> getByUsername(String login);
    User update(User user);
    void updateTokens(User user);

    default boolean isReady(){
        return true;
    }
}
