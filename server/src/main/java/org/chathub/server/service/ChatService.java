package org.chathub.server.service;

import org.chathub.server.model.Chat;
import org.chathub.server.model.Message;
import org.chathub.server.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public void saveMessage(Message mes, String chatName) {
        if(chatName == null){
            chatName = "Public";
        }
        Chat chat = chatRepository.findByName(chatName).orElse(null);
        if(chat == null){
            chat = new Chat("Public", new ArrayList<Message>());
        }
        chat.getMessages().add(mes);
        chatRepository.save(chat);
    }

    public Chat getPublicChat(){
        return chatRepository.findByName("Public").orElse(null);
    }

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
