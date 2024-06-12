package org.chathub.client.service;

import org.apache.zookeeper.*;

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

    public String selectBestServer() throws KeeperException, InterruptedException {
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

        return bestServerIp;
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
}