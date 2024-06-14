package org.chathub.server.service;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class ChatStompClient {

    private WebSocketStompClient stompClient;
    private StompSession session;

    public ChatStompClient(String host) throws ExecutionException, InterruptedException {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        CompletableFuture<StompSession> sessionAsync = stompClient.connectAsync(host, new ChatStompSessionHandler());
        session = sessionAsync.get();
        session.subscribe("/topic/public", new ChatStompSessionHandler());
    }

    public WebSocketStompClient getStompClient() {
        return stompClient;
    }

    public void setStompClient(WebSocketStompClient stompClient) {
        this.stompClient = stompClient;
    }

    public StompSession getSession() {
        return session;
    }

    public void setSession(StompSession session) {
        this.session = session;
    }
}
