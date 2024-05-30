package com.example.chathub.controller;

import com.example.chathub.model.*;
import com.example.chathub.service.ChatService;
import com.example.chathub.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MesController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtService jwtService;

    private final SimpMessageSendingOperations mesTemplate;

    public MesController(SimpMessageSendingOperations mesTemplate) {
        this.mesTemplate = mesTemplate;
    }

    @MessageMapping("/mes.send")
    @SendTo("/topic/public")
    public Message handleMessage(@Payload MessageSent messageSent) {
        if(jwtService.validateToken(messageSent.getToken())) {
            String user = jwtService.getUser(messageSent.getToken());
            Message mes = new Message(messageSent.getMessage(), user, MessageType.USER);
            chatService.saveMessage(mes, null);
            return mes;
        }
        return null;
    }

    @MessageMapping("/mes.addUser")
    @SendTo("/topic/public")
    public Message joinSession(@Payload MessageSent messageSent, SimpMessageHeaderAccessor headerAccessor) {
        if(jwtService.validateToken(messageSent.getToken())) {
            String user = jwtService.getUser(messageSent.getToken());
            Message mes = new Message(user + " joined session", user, MessageType.CONNECT);
            chatService.saveMessage(mes, null);
            headerAccessor.getSessionAttributes().put("token", messageSent.getToken());
            return mes;
        }
        return null;
    }
}
