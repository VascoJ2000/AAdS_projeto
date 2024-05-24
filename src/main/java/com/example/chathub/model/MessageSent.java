package com.example.chathub.model;

public class MessageSent {
    private String message;
    private String chatId;

    public MessageSent(String message, String chatId) {
        this.message = message;
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
