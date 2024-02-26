package com.example.rabbitmqtest.repository;

import com.example.rabbitmqtest.model.Message;
import com.example.rabbitmqtest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findAllByUser(User user);
}
