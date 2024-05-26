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
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    private final SimpMessageSendingOperations mesTemplate;

    public ChatController(SimpMessageSendingOperations mesTemplate) {
        this.mesTemplate = mesTemplate;
    }

    @GetMapping
    public Chat getChat(@RequestParam String id) {
        return chatService.findById(id);
    }

    @PostMapping
    public String createChat(@RequestBody String user1, @RequestBody String user2) {
        List<String> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        List<Message> messages = new ArrayList<>();
        Chat newChat = new Chat(messages, users);
        chatService.save(newChat);
        return "Message successfully sent";
    }

    @GetMapping("/all")
    public List<Chat> getChats(@RequestParam String user) {
        return chatService.findByUser(user);
    }

    @MessageMapping("/mes.send")
    public void handleMessage(@Payload MessageSent messageSent) {
        Chat chatHistory = chatService.findById(messageSent.getChatId());
        List<String> users = chatHistory.getUsers();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        String user_id = userService.findByEmail(user).getId();
        if (users.isEmpty() || users.contains(user)) {
            Message mes = new Message(messageSent.getMessage(), user_id, MessageType.USER);
            mesTemplate.convertAndSend("/topic/" + messageSent.getChatId(), mes);
        }
    }

    @MessageMapping("/user.add")
    public void joinSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        String user_id = userService.findByEmail(user).getId();
        List<Chat> userChats = chatService.findByUser(user);
        Message mes = new Message(user + " joined session", user_id, MessageType.JOIN);
        if (!userChats.isEmpty()) {
            for (Chat chat : userChats) {
                mesTemplate.convertAndSend("/topic/" + chat.getId(), mes);
            }
        }
    }
}
