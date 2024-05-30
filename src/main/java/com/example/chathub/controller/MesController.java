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
    public Message handleMessage(@Payload MessageSent messageSent, SimpMessageHeaderAccessor headerAccessor) {
        String token = (String) headerAccessor.getSessionAttributes().get("token");
        if (token != null) {
            if (jwtService.validateToken(token)) {
                String user = jwtService.getUser(token);
                Message mes = new Message(messageSent.getMessage(), user, MessageType.USER);
                chatService.saveMessage(mes, null);
                return mes;
            }
        }
        return null;
    }

    @MessageMapping("/mes.addUser")
    @SendTo("/topic/public")
    public Message joinSession(@Payload JwtMessage jwtMessage, SimpMessageHeaderAccessor headerAccessor) {
        if(jwtService.validateToken(jwtMessage.getToken())) {
            String user = jwtService.getUser(jwtMessage.getToken());
            Message mes = new Message(user + " joined session", user, MessageType.CONNECT);
            chatService.saveMessage(mes, null);
            headerAccessor.getSessionAttributes().put("token", jwtMessage.getToken());
            return mes;
        }
        return null;
    }
}
