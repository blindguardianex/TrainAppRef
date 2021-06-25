package com.smartru.receiver;

import com.smartru.rabbit.configuration.TaskQueueRabbitStarter;
import com.smartru.telegram.PrimeNumberCheckTelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
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
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            PrimeNumberCheckTelegramBot telegramBot = ctx.getBean(PrimeNumberCheckTelegramBot.class);
            botsApi.registerBot(telegramBot);
            log.info("Telegram bot successfully started!");
        } catch (TelegramApiException e) {
            log.error("Telegram bot not started...");
            e.printStackTrace();
        }
    }
}
