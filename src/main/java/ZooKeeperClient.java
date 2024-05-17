
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.*;

public class ZooKeeperClient {
    private static final String ZOOKEEPER_HOST = "localhost:2181"; // Configurar
    private ZooKeeper zooKeeper;

    public ZooKeeperClient() throws IOException {
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

    public void registerServer(String path, byte[] data) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(path, false);
        if (stat == null) {
            zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } else {
            zooKeeper.setData(path, data, stat.getVersion());
        }
    }

    public String selectBestServer() throws KeeperException, InterruptedException {
        List<String> servers = zooKeeper.getChildren("/servers", false);
        String bestServer = null;
        int minLoad = Integer.MAX_VALUE;

        for (String server : servers) {
            byte[] serverData = zooKeeper.getData("/servers/" + server, false, null);
            int load = Integer.parseInt(new String(serverData));
            if (load < minLoad) {
                minLoad = load;
                bestServer = server;
            }
        }

        return bestServer;
    }
}
