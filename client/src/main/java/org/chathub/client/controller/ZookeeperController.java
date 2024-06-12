package org.chathub.client.controller;

import jakarta.annotation.PostConstruct;
import org.apache.zookeeper.KeeperException;
import org.chathub.client.service.ZooKeeperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ZookeeperController {

    private ZooKeeperService zooKeeperService;

    @PostConstruct
    public void init() throws IOException {
        this.zooKeeperService = new ZooKeeperService();
    }

    @GetMapping("/api/server")
    public String getServers() throws InterruptedException, KeeperException {
        return zooKeeperService.selectBestServer();
    }
}
