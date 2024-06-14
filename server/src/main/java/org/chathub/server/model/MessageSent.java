package org.chathub.server.model;

public class MessageSent {
    private String message;
    private String chat;

    public MessageSent(String message, String chat) {
        this.message = message;
        this.chat = chat;
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
}
