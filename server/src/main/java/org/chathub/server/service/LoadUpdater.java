package org.chathub.server.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@Component
public class LoadUpdater {

    private final ZooService zooService;

    public LoadUpdater(ZooService zooService) {
        this.zooService = zooService;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentLoad() {
        int currentLoad = calculateLoad();
        zooService.updateLoad(currentLoad);
    }

    private int calculateLoad() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double systemLoad = osBean.getSystemLoadAverage();
        return Math.min(100, (int) (systemLoad * 100.0));
    }
}
