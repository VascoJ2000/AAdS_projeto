package org.chathub.server.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.Watcher;
import org.chathub.server.model.Message;
import org.chathub.server.model.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ZooService {

    private static final String ZOOKEEPER_HOST = "localhost:2181";
    private static final String BASE_PATH = "/servers";
    private static final String ELECTION_PATH = "/leader";
    private static final String CHAT_PATH = "/chat";

    private CuratorFramework client;
    private LeaderLatch leaderLatch;

    private final SimpMessageSendingOperations messagingTemplate;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    private String serverAddress;
    private String serverPath;
    private String serverData;
    private AtomicInteger currentLoad = new AtomicInteger(0);

    @Autowired
    private ChatService chatService;

    public ZooService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void start() {
        client = CuratorFrameworkFactory.newClient(ZOOKEEPER_HOST, new ExponentialBackoffRetry(1000, 3));
        client.start();

        try {
            serverAddress = InetAddress.getLocalHost().getHostAddress();
            serverPath = BASE_PATH + "/" + applicationName;
            serverData = serverAddress + ":" + serverPort + ":" + currentLoad.get();
            if (client.checkExists().forPath(serverPath) == null) {
                client.create().creatingParentContainersIfNeeded().forPath(serverPath, serverData.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(client.checkExists().forPath(ELECTION_PATH) == null) {
                client.create().creatingParentContainersIfNeeded().forPath(ELECTION_PATH);
            }
            leaderLatch = new LeaderLatch(client, ELECTION_PATH, serverPath);
            leaderLatch.start();
            if (client.checkExists().forPath(CHAT_PATH) == null) {
                client.create().creatingParentContainersIfNeeded().forPath(CHAT_PATH);
            }
            syncMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLoad(int load) {
        try {
            currentLoad.set(load);
            serverData = serverAddress + ":" + serverPort + ":" + load;
            client.setData().forPath(serverPath, serverData.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLeader(){
        return leaderLatch.hasLeadership();
    }

    public void sendMessage(Message message){
        String mes = message.getMessage() + "/:/" + message.getUser_id() + "/:/" + message.getType();
        try {
            client.setData().forPath(CHAT_PATH, mes.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncMessages() throws Exception {
        client.getData().usingWatcher((CuratorWatcher) event -> {
            if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                byte[] data = client.getData().forPath(CHAT_PATH);
                // Turn bytes back into message
                String[] dataParts = new String(data).split("/:/");
                Message message = new Message(dataParts[0], dataParts[1], MessageType.valueOf(dataParts[2]));
                System.out.println(message);
                System.out.println(isLeader());
                if (isLeader() && message.getType() == MessageType.USER) {
                    chatService.saveMessage(message, null);
                }
                // Broadcast the message to WebSocket clients
                messagingTemplate.convertAndSend("/topic/public", message);
            }
            // Re-add the watcher
            syncMessages();
        }).forPath(CHAT_PATH);
    }

    @PreDestroy
    public void stop() {
        try {
            client.delete().forPath(serverPath);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
