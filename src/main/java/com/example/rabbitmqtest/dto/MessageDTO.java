package com.example.rabbitmqtest.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private String content;
    private String routingKey;
}
