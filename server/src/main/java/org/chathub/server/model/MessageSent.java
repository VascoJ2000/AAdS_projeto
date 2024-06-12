package org.chathub.server.model;

public class MessageSent {
    private String message;
    private String chat;
    private String sender;

    public MessageSent(String message, String chat, String sender) {
        this.message = message;
        this.chat = chat;
        this.sender = sender;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
