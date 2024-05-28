package com.example.chathub.model;

public class MessageSent {
    private String message;
    private String chat;
    private String token;

    public MessageSent(String message, String chat, String token) {
        this.message = message;
        this.chat = chat;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chatId) {
        this.chat = chatId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
