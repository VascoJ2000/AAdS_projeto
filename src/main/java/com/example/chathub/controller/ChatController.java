package com.example.chathub.controller;

import com.example.chathub.model.Chat;
import com.example.chathub.model.Message;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @GetMapping
    public Chat chat(@RequestParam String id) {
        ArrayList<Message> chatHistory = new ArrayList<Message>();
        return new Chat(chatHistory);
    }

    @PostMapping
    public String chat(@RequestBody Message mes) {
        return "Message successfully sent";
    }
}
