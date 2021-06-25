package com.smartru.rabbit.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbit.task.exchange}")
    private String taskExchangeName;
    @Value("${rabbit.task.queue}")
    private String taskQueueName;
    @Value("${rabbit.task.telegram.queue}")
    private String telegramTaskQueueName;

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange taskExchange(){
        return new TopicExchange(taskExchangeName,true,false);
    }

    @Bean
    @Qualifier("httpTaskQueue")
    public Queue httpTaskQueue(){
        return new Queue(taskQueueName, true, false, false);
    }

    @Bean
    @Qualifier("telegramTaskQueue")
    public Queue telegramTaskQueue(){
        return new Queue(telegramTaskQueueName, true, false, false);
    }
}
