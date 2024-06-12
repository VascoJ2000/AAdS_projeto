package org.chathub.server.model;

import java.time.Instant;

public class Message {
    private String message;
    private Instant date;
    private String user_id;
    private MessageType type;

    public Message(String message, String user_id, MessageType type) {
        this.message = message;
        this.date = Instant.now();
        this.user_id = user_id;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public Instant getDate() {
        return date;
    }

    public String getUser_id() {
        return user_id;
    }

    public MessageType getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Message[Message=%s, Date=%s, User_id=%s]", message, date, user_id);
    }
}
