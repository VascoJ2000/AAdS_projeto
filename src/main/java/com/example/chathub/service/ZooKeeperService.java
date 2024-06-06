package com.example.chathub.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class ZooKeeperService {
    private static final String ZOOKEEPER_HOST = "localhost:2181";
    private ZooKeeper zooKeeper;

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
                    List<String> servers = zooKeeper.getChildren("/servers", false);
                    if (servers.isEmpty()) {

                    } else {

                    }
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
}