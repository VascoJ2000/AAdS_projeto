package org.chathub.server.model;

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
    private Instant creationDate;
    private Instant modifiedDate;

    public Chat(String name, List<Message> messages) {
        this.name = name;
        this.messages = messages;
        this.creationDate = Instant.now();
        this.modifiedDate = Instant.now();
    }

    public String getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
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
}
