package org.chathub.server.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ZooKeeperService {
    private static final String ZOOKEEPER_HOST = "localhost:2181";
    private static final String ELECTION_NAMESPACE = "/election";
    private ZooKeeper zooKeeper;
    private ChatStompClient chatStompClient;

    @Value("${server.address:localhost}")
    private String serverAddress;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    public ZooKeeperService() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_HOST, 3000, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.Expired) {
                try {
                    reconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void reconnect() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_HOST, 3000, null);
    }

    public void registerServer(String path, String ip, int load) throws KeeperException, InterruptedException {
        String data = ip + ":" + load;
        Stat stat = zooKeeper.exists(path, false);
        if (stat == null) {
            zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } else {
            zooKeeper.setData(path, data.getBytes(), stat.getVersion());
        }
    }

    public void updateServerLoad(String path, String ip, int load) throws KeeperException, InterruptedException {
        String data = ip + ":" + load;
        Stat stat = zooKeeper.exists(path, false);
        if (stat != null) {
            zooKeeper.setData(path, data.getBytes(), stat.getVersion());
        }
    }

    public void watchServers() throws KeeperException, InterruptedException {
        zooKeeper.getChildren("/servers", event -> {
            if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                try {
                    List<String> servers = zooKeeper.getChildren("/servers/leader", false);
                    if (servers.isEmpty()) {
                        electLeader();
                        syncWithLeader();
                    }else{
                        syncWithLeader();
                    }
                } catch (KeeperException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void electLeader() throws KeeperException, InterruptedException {
        List<String> servers = zooKeeper.getChildren("/servers", false);
        String bestServer = null;
        int minLoad = Integer.MAX_VALUE;
        String bestServerIp = null;

        for (String server : servers) {
            byte[] serverData = zooKeeper.getData("/servers/" + server, false, null);
            String[] dataParts = new String(serverData).split(":");
            String ip = dataParts[0] + ':' + dataParts[1];
            int load = Integer.parseInt(dataParts[2]);

            if (load < minLoad) {
                minLoad = load;
                bestServer = server;
                bestServerIp = ip;
            }
        }

        if(Objects.equals(bestServer, applicationName)){
            String path = "/servers/leader";
            String data = serverAddress + ":" + serverPort;
            zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }
    }

    private void syncWithLeader() throws KeeperException, InterruptedException, ExecutionException {
        byte[] leaderData = zooKeeper.getData("/servers/leader", false, null);
        String leaderIp = new String(leaderData);

        String url = "ws://" + leaderIp + "/chat-socket";
        chatStompClient = new ChatStompClient(url);
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public ChatStompClient getChatStompClient() {
        return chatStompClient;
    }
}