package ru.vaschenko.ServiceDiscovery.services;

import feign.Feign;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.client.ComputingNodeClient;
import ru.vaschenko.ServiceDiscovery.dto.SubTaskRequest;
import ru.vaschenko.ServiceDiscovery.registry.NodeInformation;
import ru.vaschenko.ServiceDiscovery.registry.NodeRegistry;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {
    private final Feign.Builder feignBuilder;
    private final TaskManagementService taskManagementService;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void pollNodes() {
        List<NodeInformation> allNodes = NodeRegistry.getAllNodes();
        for (NodeInformation node : allNodes) {
            log.info("notify node {}", node.getNodeUrl());
            try{
                getComputingNodeClientForHost(node.getNodeUrl()).ping();
            }catch (FeignException e){
                //перенаправляем его задачку
                if(node.getSubTaskRequest() != null) redirectSubTask(node.getSubTaskRequest());

                //удаляем узел из реестра
                NodeRegistry.removeNode(node.getNodeUrl());

            }
        }
    }

    public void redirectSubTask(SubTaskRequest subTaskRequest){
        taskManagementService.pushSubtaskQueue(subTaskRequest);
    }

    public ComputingNodeClient getComputingNodeClientForHost(String host) {
        return feignBuilder.target(ComputingNodeClient.class, host);
    }
}
