package org.chathub.client.controller;

import org.apache.zookeeper.KeeperException;
import org.chathub.client.service.ZooKeeperService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/api/server")
public class ZookeeperController {

    private ZooKeeperService zooKeeperService;

    @Bean
    public ZooKeeperService zooKeeperService() throws IOException {
        return new ZooKeeperService();
    }

    @GetMapping
    public String getServers() throws InterruptedException, KeeperException {
        return zooKeeperService.selectBestServer();
    }
}
