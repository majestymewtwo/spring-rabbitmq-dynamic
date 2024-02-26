package com.example.rabbitmqtest.controller;

import com.example.rabbitmqtest.dto.MessageDTO;
import com.example.rabbitmqtest.dto.UserDTO;
import com.example.rabbitmqtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService service;
    @GetMapping
    public String test() {
        return "this works";
    }
    @PostMapping("/newUser")
    public ResponseEntity<String> newUser(@RequestBody UserDTO userDTO) {
        service.saveUser(userDTO);
        return ResponseEntity.ok("Created new user");
    }
    @GetMapping("/getMessages")
    public List<MessageDTO> getMessages(@RequestParam String routingKey) {
        return service.getMessages(routingKey);
    }
    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO messageDTO) {
        service.sendMessage(messageDTO);
        return ResponseEntity.ok("Sent message successfully");
    }
    @PostMapping("/saveMessage")
    public ResponseEntity<String> saveMessage(@RequestBody MessageDTO messageDTO) {
        service.saveMessage(messageDTO);
        return ResponseEntity.ok("Saved message to DB");
    }
}
