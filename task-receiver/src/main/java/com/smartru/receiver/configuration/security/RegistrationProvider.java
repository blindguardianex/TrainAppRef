package com.smartru.receiver.configuration.security;

import com.smartru.common.entity.User;
import com.smartru.common.exceptions.EntityAlreadyExists;
import com.smartru.common.service.jpa.UserService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.BadAttributeValueExpException;
import java.util.Optional;

@Slf4j
@Component
public class RegistrationProvider {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;

    public User registry(User user) throws EntityAlreadyExists {
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            user = userService.add(user);
            return user;
        } catch (EntityAlreadyExists ex) {
            log.error("Пользователь с таким именем уже существует");
            throw new EntityAlreadyExists("Пользователь с таким именем уже существует");
        }
    }
}
