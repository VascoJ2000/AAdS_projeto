package com.example.chathub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "Chats")
public class Chat {

    @Id
    private String id;

    private List<Message> messages;
    private List<String> users;
    private Instant creationDate;
    private Instant modifiedDate;

    public Chat(List<Message> messages, List<String> users) {
        this.messages = messages;
        this.users = users;
        this.creationDate = Instant.now();
        this.modifiedDate = Instant.now();
    }

    public String getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<String> getUsers() {
        return users;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void addUser(String user) {
        this.users.add(user);
    }

    public void kickUser(String user) {
        this.users.remove(user);
    }
}
