package com.example.rabbitmqtest.publisher;

import com.example.rabbitmqtest.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.json.key}")
    private String jsonRoutingKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonProducer.class);
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMessage(User user) {
        LOGGER.info(String.format("JSON Message sent -> %s", user.toString()));
        rabbitTemplate.convertAndSend(exchange, jsonRoutingKey, user);
    }
}
