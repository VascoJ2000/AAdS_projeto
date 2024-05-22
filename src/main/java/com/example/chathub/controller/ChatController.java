package com.example.chathub.controller;

import com.example.chathub.model.Chat;
import com.example.chathub.model.Message;
import com.example.chathub.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

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

    @PostMapping("/mes")
    public String sendMessage(@RequestBody String mes, @RequestBody String chat, @RequestBody String user) {
        Chat chatHistory = chatService.findById(chat);
        List<String> users = chatHistory.getUsers();
        if (!users.contains(user)) {
            return "User not allowed";
        }
        return "Message successfully sent";
    }
}
