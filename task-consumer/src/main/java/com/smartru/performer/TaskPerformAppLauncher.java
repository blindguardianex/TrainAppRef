package com.smartru.performer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Приложение, отвечающее за аутентификацию/авторизацию пользователя
 * и прием задач от него
 */
@Slf4j
@SpringBootApplication
@EnableJpaAuditing
@EntityScan("com.smartru.common.entity")
@EnableJpaRepositories("com.smartru.jpa.repository")
@ComponentScan("com.smartru")
public class TaskPerformAppLauncher {

    private static ApplicationContext ctx;

    public static void main(String[] args){
        ctx = SpringApplication.run(TaskPerformAppLauncher.class, args);
        log.info("Application started!");
    }
}
