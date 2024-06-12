package org.chathub.server.config;

import org.chathub.server.model.Message;
import org.chathub.server.model.MessageType;
import org.chathub.server.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chathub.server.service.UserService;
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
    private UserService userService;

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String name = (String) headerAccessor.getSessionAttributes().get("token");
        if (name != null) {
            if(userService.findByEmail(name) != null) {
                log.info("user disconnected: {}", name);
                Message mes = new Message(name + " left session", name, MessageType.DISCONNECT);
                messagingTemplate.convertAndSend("/topic/public", mes);
            }
        }
    }
}
