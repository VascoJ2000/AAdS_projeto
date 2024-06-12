package org.chathub.server.controller;

import org.chathub.server.model.Message;
import org.chathub.server.model.MessageSent;
import org.chathub.server.model.MessageType;
import org.chathub.server.model.User;
import org.chathub.server.service.ChatService;
import org.chathub.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MesController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @MessageMapping("/mes.send")
    @SendTo("/topic/public")
    public Message handleMessage(@Payload MessageSent messageSent) {
        String name = messageSent.getSender();
        if (name != null) {
            if (userService.findByEmail(name) != null) {
                Message mes = new Message(messageSent.getMessage(), name, MessageType.USER);
                chatService.saveMessage(mes, null);
                return mes;
            }
        }
        return null;
    }

    @MessageMapping("/mes.addUser")
    @SendTo("/topic/public")
    public Message joinSession(@Payload String username) {
        User user = userService.findByEmail(username);
        if(user != null) {
            Message mes = new Message(username + " joined session", username, MessageType.CONNECT);
            chatService.saveMessage(mes, null);
            return mes;
        }
        return null;
    }
}
