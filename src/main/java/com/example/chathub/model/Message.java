package com.example.chathub.model;

import java.time.LocalDateTime;

public class Message {
    private String message;
    private LocalDateTime date;
    private String user_id;

    public Message(String message, String user_id) {
        this.message = message;
        this.date = LocalDateTime.now();
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getUser_id() {
        return user_id;
    }

    @Override
    public String toString() {
        return String.format("Message[Message=%s, Date=%s, User_id=%s]", message, date, user_id);
    }
}
