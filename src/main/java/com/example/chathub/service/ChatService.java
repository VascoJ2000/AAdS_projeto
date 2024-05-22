package com.example.chathub.service;

import com.example.chathub.model.Chat;
import com.example.chathub.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    public Chat findById(String id) {
        Optional<Chat> chat = chatRepository.findById(id);
        return chat.orElse(null);
    }

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public void delete(String id) {
        chatRepository.deleteById(id);
    }

    public List<Chat> findByUser(String id) {
        return chatRepository.findByUsers(id);
    }
}
