package org.chathub.server.config;

import org.chathub.server.service.ZooKeeperService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ZookeeperConfig {

    private ZooKeeperService zooKeeperService;
    private String serverNodePath;
    private AtomicInteger currentLoad = new AtomicInteger(0);

    @Value("${server.address:localhost}")
    private String serverAddress;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    public ZooKeeperService zooKeeperService() throws IOException {
        return new ZooKeeperService();
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println(serverPort);
            System.out.println(serverAddress);
            this.zooKeeperService = zooKeeperService();
            serverNodePath = "/servers/" + applicationName;
            zooKeeperService.registerServer(serverNodePath, serverAddress + ':' + serverPort, currentLoad.get());
            measureSystemLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLoad(boolean increase) {
        int newLoad = increase ? currentLoad.incrementAndGet() : currentLoad.decrementAndGet();
        try {
            zooKeeperService.updateServerLoad(serverNodePath, serverAddress + ':' + serverPort, newLoad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void measureSystemLoad() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double systemLoad = osBean.getSystemLoadAverage();
        int adjustedLoad = Math.min(100, (int) (systemLoad * 100.0));
        updateLoad(adjustedLoad>currentLoad.get());
    }
}
