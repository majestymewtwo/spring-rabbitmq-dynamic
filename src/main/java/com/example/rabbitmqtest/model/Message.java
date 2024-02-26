package com.example.rabbitmqtest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 4000)
    private String content;
    @ManyToOne
    private User user;
}
