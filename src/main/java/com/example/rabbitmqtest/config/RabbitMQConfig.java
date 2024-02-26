package com.example.rabbitmqtest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    private ConnectionFactory connectionFactory;
    @Autowired
    public RabbitMQConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    /* Bean for Rabbit MQ Exchange  */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }
    /* Bean for ConnectionFactory, RabbitTemplate and RabbitAdmin automatically configured by Spring Boot Auto Configuration. But to send JSON
    * Messages the MessageConverter of RabbitMQTemplate must be changed */
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
    /* Using AMQP Admin for Dynamic Queues */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory);
    }
}
