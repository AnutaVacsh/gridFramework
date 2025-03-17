package ru.vaschenko.ServiceDiscovery.services;

import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.dto.NodeRegisterDto;
import ru.vaschenko.ServiceDiscovery.dto.enam.NodeType;
import ru.vaschenko.ServiceDiscovery.registry.NodeRegistry;

@Service
public class NodeManagementService {
    public void nodeRegister(NodeRegisterDto nodeRegisterDto) {
        if (nodeRegisterDto.type() == NodeType.DISTRIBUTION) {
            NodeRegistry.setDistributionNodes(nodeRegisterDto);
        } else {
            NodeRegistry.addNode(nodeRegisterDto);
        }
    }
}
