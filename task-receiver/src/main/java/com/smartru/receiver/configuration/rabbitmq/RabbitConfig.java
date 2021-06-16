package com.smartru.receiver.configuration.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbit.task.exchange}")
    private String taskExchangeName;
    @Value("${rabbit.task.queue}")
    private String taskQueueName;

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange taskExchange(){
        return new TopicExchange(taskExchangeName,true,false);
    }

    @Bean
    public Queue taskQueue(){
        return new Queue(taskQueueName, true, false, false);
    }
}
