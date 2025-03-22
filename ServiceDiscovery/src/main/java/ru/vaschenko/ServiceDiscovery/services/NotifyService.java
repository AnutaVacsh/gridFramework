package ru.vaschenko.ServiceDiscovery.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.client.ComputingNodeClient;
import ru.vaschenko.ServiceDiscovery.registry.NodeInformation;
import ru.vaschenko.ServiceDiscovery.registry.NodeRegistry;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {
    private final ComputingNodeClient computingNodeClient;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void pollNodes() {
        List<NodeInformation> allNodes = NodeRegistry.getAllNodes();
        for (NodeInformation node : allNodes) {
            log.info("notify node {}", node.getNodeUrl());
            computingNodeClient.ping();
        }
    }
}
