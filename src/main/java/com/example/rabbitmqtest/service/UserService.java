package com.example.rabbitmqtest.service;

import com.example.rabbitmqtest.dto.MessageDTO;
import com.example.rabbitmqtest.dto.UserDTO;
import com.example.rabbitmqtest.model.Message;
import com.example.rabbitmqtest.model.User;
import com.example.rabbitmqtest.repository.MessageRepo;
import com.example.rabbitmqtest.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepo userRepo;



    private MessageRepo messageRepo;
    private AmqpAdmin admin;
    private RabbitTemplate rabbitTemplate;
    private TopicExchange exchange;
    @Autowired
    public UserService(UserRepo userRepo, MessageRepo messageRepo, AmqpAdmin admin, RabbitTemplate rabbitTemplate, TopicExchange exchange) {
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
        this.admin = admin;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void saveUser(UserDTO userDTO) {
        if(userRepo.findByRoutingKey(userDTO.getRoutingKey()).isPresent())
            throw new RuntimeException("Routing key must be unique");
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setMessages(new ArrayList<>());
        userRepo.save(user);
    }
    public List<MessageDTO> getMessages(String routingKey) {
        User user = userRepo.findByRoutingKey(routingKey).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        return messageRepo.findAllByUser(user).stream().map(message -> mapToDTO(message)).toList();
    }
    public void sendMessage(MessageDTO messageDTO) {
        String queueName = messageDTO.getRoutingKey() + "-queue";
        if(admin.getQueueProperties(queueName) == null) {
            Queue queue = new Queue(queueName, true, false, false);
            Binding binding = BindingBuilder.bind(queue)
                    .to(exchange)
                    .with(messageDTO.getRoutingKey());
            admin.declareQueue(queue);
            admin.declareBinding(binding);
        }
        rabbitTemplate.convertAndSend(exchange.getName(), messageDTO.getRoutingKey(), messageDTO);
    }
    public void saveMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        User user = userRepo.findByRoutingKey(messageDTO.getRoutingKey()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        message.setUser(user);
        messageRepo.save(message);
    }
    public MessageDTO mapToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        BeanUtils.copyProperties(message, messageDTO);
        messageDTO.setRoutingKey(message.getUser().getRoutingKey());
        return messageDTO;
    }
}
