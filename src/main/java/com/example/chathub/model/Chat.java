package com.example.chathub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Chats")
public class Chat {

    @Id
    private String id;

    private ArrayList<Message> messages;

    public Chat(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
