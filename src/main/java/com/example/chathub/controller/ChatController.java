package com.example.chathub.controller;

import com.example.chathub.model.Chat;
import com.example.chathub.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/api/chat")
    public Chat getChat() {
        return chatService.getPublicChat();
    }
}
