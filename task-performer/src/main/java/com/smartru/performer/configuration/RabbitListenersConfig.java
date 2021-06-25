package com.smartru.performer.configuration;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitListenersConfig {

    @Value("${spring.rabbitmq.username}")
    private String login;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.listener.simple.prefetch}")
    private int prefetchCount;
    @Value("${spring.rabbitmq.listener.simple.concurrency}")
    private int concurrentConsumers;
    @Value("${spring.rabbitmq.listener.simple.max-concurrency}")
    private int maxConcurrentConsumers;
    @Value("${spring.rabbitmq.listener.simple.start-consumer-min-interval}")
    private long startConsumerMinInterval;
    @Value("${spring.rabbitmq.listener.simple.stop-consumer-min-interval}")
    private long stopConsumerMinInterval;

//    @Bean
//    public MessageConverter messageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses(host);
        factory.setPort(port);
        factory.setUsername(login);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter converter){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(prefetchCount);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        factory.setStartConsumerMinInterval(startConsumerMinInterval);
        factory.setStopConsumerMinInterval(stopConsumerMinInterval);
        factory.setMessageConverter(converter);
        return factory;
    }
}
