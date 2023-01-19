package com.example.code.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration
{
    @Value("${rabbitmq.queue.producer.name}")
    private String producerQueue;
    @Value("${rabbitmq.exchange.producer.name}")
    private String producerExchange;
    @Value("${rabbitmq.producer.routing.key}")
    private String producerRoutingKey;

    @Value("${rabbitmq.queue.consumer.name}")
    private String consumerQueue;
    @Value("${rabbitmq.exchange.consumer.name}")
    private String consumerExchange;
    @Value("${rabbitmq.consumer.routing.key}")
    private String consumerRoutingKey;

    @Value("${spring.rabbitmq.host}")
    String host;
    @Value("${spring.rabbitmq.username}")
    String username;
    @Value("${spring.rabbitmq.password}")
    String password;

    @Bean
    Queue queue() {
        return new Queue(producerQueue);
    }

    @Bean
    public Queue consumerQueue(){
        return new Queue(consumerQueue);
    }
    @Bean
    Exchange producerExchange() {
        return ExchangeBuilder.directExchange(producerExchange).build();
    }

    @Bean
    Exchange consumerExchange() {
        return ExchangeBuilder.directExchange(consumerExchange).build();
    }
    @Bean
    Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(producerExchange())
                .with(producerRoutingKey)
                .noargs();
    }

    @Bean
    Binding consumerBinding() {
        return BindingBuilder
                .bind(consumerQueue())
                .to(consumerExchange())
                .with(consumerRoutingKey)
                .noargs();
    }

    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
