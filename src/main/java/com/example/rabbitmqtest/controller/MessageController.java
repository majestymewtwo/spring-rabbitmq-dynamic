package com.example.rabbitmqtest.controller;

import com.example.rabbitmqtest.dto.User;
import com.example.rabbitmqtest.publisher.RabbitMQJsonProducer;
import com.example.rabbitmqtest.publisher.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MessageController {
    private final RabbitMQProducer producer;
    private final RabbitMQJsonProducer jsonProducer;
    @Autowired
    public MessageController(RabbitMQProducer producer, RabbitMQJsonProducer jsonProducer) {
        this.producer = producer;
        this.jsonProducer = jsonProducer;
    }
    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        producer.sendMessage(message);
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }
    @PostMapping("/publish/json")
    public ResponseEntity<String> sendJsonMessage(@RequestBody User user) {
        jsonProducer.sendMessage(user);
        return ResponseEntity.ok("JSON Message sent to RabbitMQ");
    }

}
