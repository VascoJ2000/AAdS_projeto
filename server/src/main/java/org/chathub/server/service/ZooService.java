package org.chathub.server.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ZooService {

    private static final String ZOOKEEPER_HOST = "localhost:2181";
    private static final String BASE_PATH = "/servers";
    private static final String ELECTION_PATH = "/leader";

    private CuratorFramework client;
    private LeaderLatch leaderLatch;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    private String serverAddress;
    private String serverPath;
    private String serverData;
    private AtomicInteger currentLoad = new AtomicInteger(0);

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

        leaderLatch = new LeaderLatch(client, ELECTION_PATH, serverPath);
        try {
            leaderLatch.start();
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

    public String getLeader() throws Exception {
        return leaderLatch.getLeader().toString();
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
