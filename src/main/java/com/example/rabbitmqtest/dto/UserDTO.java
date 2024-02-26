package com.example.rabbitmqtest.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String routingKey;
}
