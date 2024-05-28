package com.example.chathub.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "Chats")
public class Chat {

    @Id
    private String id;

    private String name;
    private List<Message> messages;
    private List<String> users;
    private Instant creationDate;
    private Instant modifiedDate;

    public Chat(String name, List<Message> messages, List<String> users) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
