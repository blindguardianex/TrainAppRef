package com.smartru.receiver;

import com.smartru.common.bot.BotStarter;
import com.smartru.rabbit.configuration.TaskQueueRabbitStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
@EntityScan("com.smartru.common.entity")
@EnableJpaRepositories("com.smartru.jpa.repository")
@ComponentScan("com.smartru")
public class TaskReceiverAppLauncher {

    private static ApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(TaskReceiverAppLauncher.class, args);
        testRedis();
        taskRabbitQueueStarting();
        telegramBotStarting();
        log.info("Application started!");
    }

    private static void taskRabbitQueueStarting(){
        TaskQueueRabbitStarter queueStarter = ctx.getBean(TaskQueueRabbitStarter.class);
        queueStarter.declareTaskQueue();
    }

    private static void testRedis(){
        try(Jedis jedis = ctx.getBean(JedisPool.class).getResource()) {
            assert (jedis.ping().equals("PONG"));
            log.info("Jedis is ready!");
        } catch (JedisConnectionException ex) {
            log.error("Jedis is not started!");
            System.exit(1);
        }
    }

    private static void telegramBotStarting(){
        try {
            BotStarter telegramBotStarter = (BotStarter) ctx.getBean("telegramBotStarter");
            telegramBotStarter.start();
        }catch (NoSuchBeanDefinitionException ex){
            log.warn("Starting application without Telegram module!");
        }
    }
}
