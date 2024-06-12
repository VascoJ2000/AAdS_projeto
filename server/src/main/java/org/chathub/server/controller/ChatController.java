package org.chathub.server.controller;

import org.chathub.server.model.Chat;
import org.chathub.server.service.ChatService;
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
