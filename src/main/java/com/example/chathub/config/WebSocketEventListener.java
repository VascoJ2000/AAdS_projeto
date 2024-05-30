package com.example.chathub.config;

import com.example.chathub.model.Message;
import com.example.chathub.model.MessageType;
import com.example.chathub.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private JwtService jwtService;

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String token = (String) headerAccessor.getSessionAttributes().get("token");
        if (token != null) {
            if(jwtService.validateToken(token)) {
                String user = jwtService.getUser(token);
                Message mes = new Message(user + " left session", user, MessageType.DISCONNECT);
                messagingTemplate.convertAndSend("/topic/public", mes);
            }
        }
    }
}
