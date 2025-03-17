package ru.vaschenko.ServiceDiscovery.registry;

import org.springframework.stereotype.Component;
import ru.vaschenko.ServiceDiscovery.dto.NodeRegisterDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NodeRegistry {
    private static String distributionNode;
    private static final Map<String, NodeInformation> nodes = new HashMap<>();


    public static void setDistributionNodes(NodeRegisterDto nodeRegisterDto) {
        distributionNode = nodeRegisterDto.nodeUrl();
    }

    public static void addNode(NodeRegisterDto nodeRegisterDto) {
        nodes.put(nodeRegisterDto.nodeUrl(), new NodeInformation().setNodeUrl(nodeRegisterDto.nodeUrl()));
    }

    public static void removeNode(String nodeUrl) {
        nodes.remove(nodeUrl);
    }

    public static String getDistributionNodes() {
        return distributionNode;
    }

    public static NodeInformation getNode(String nodeUrl) {
        return nodes.get(nodeUrl);
    }

    public static List<NodeInformation> getAllNodes() {
        return new ArrayList<>(nodes.values());
    }
}