package com.example.chathub.controller;

import com.example.chathub.model.Chat;
import com.example.chathub.model.Message;
import com.example.chathub.model.MessageSent;
import com.example.chathub.model.MessageType;
import com.example.chathub.service.ChatService;
import com.example.chathub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    private final SimpMessageSendingOperations mesTemplate;

    public ChatController(SimpMessageSendingOperations mesTemplate) {
        this.mesTemplate = mesTemplate;
    }

    @GetMapping("/api/chat")
    public Chat getChat(@RequestParam String id) {
        return chatService.findById(id);
    }

    @PostMapping("/api/chat")
    public String createChat(@RequestBody String user1, @RequestBody String user2) {
        List<String> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        List<Message> messages = new ArrayList<>();
        Chat newChat = new Chat(messages, users);
        chatService.save(newChat);
        return "Message successfully sent";
    }

    @GetMapping("/api/chat/all")
    public List<Chat> getChats(@RequestParam String user) {
        return chatService.findByUser(user);
    }

    @MessageMapping("/mes.send")
    @SendTo("/topic/public")
    public Message handleMessage(@Payload MessageSent messageSent) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Message mes = new Message(messageSent.getMessage(), user, MessageType.USER);
        System.out.println(user);
        System.out.println(mes.getMessage());
        return mes;
    }

    @MessageMapping("/mes.addUser")
    @SendTo("/topic/public")
    public Message joinSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        System.out.println(user);
        Message mes = new Message(user + " joined session", user, MessageType.JOIN);
        return mes;
    }
}
