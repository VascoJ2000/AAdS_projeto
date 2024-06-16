package org.chathub.server.controller;

import org.chathub.server.model.Message;
import org.chathub.server.model.MessageSent;
import org.chathub.server.model.MessageType;
import org.chathub.server.model.User;
import org.chathub.server.service.UserService;
import org.chathub.server.service.ZooService;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class MesController {

    @Autowired
    private UserService userService;

    @Autowired
    private ZooService zooService;

    @MessageMapping("/mes.send")
    public void handleMessage(@Payload MessageSent messageSent, SimpMessageHeaderAccessor headerAccessor) {
        String name = (String) headerAccessor.getSessionAttributes().get("token");
        if (name != null) {
            if (userService.findByEmail(name) != null) {
                Message mes = new Message(messageSent.getMessage(), name, MessageType.USER);
                zooService.sendMessage(mes);
            }
        }
    }

    @MessageMapping("/mes.addUser")
    public void joinSession(@Payload String username, SimpMessageHeaderAccessor headerAccessor) {
        User user = userService.findByEmail(username);
        if(user != null) {
            headerAccessor.getSessionAttributes().put("token", username);
            Message mes = new Message(username + " joined session", username, MessageType.CONNECT);
            zooService.updateLoad(true);
            zooService.sendMessage(mes);
        }
    }
}
