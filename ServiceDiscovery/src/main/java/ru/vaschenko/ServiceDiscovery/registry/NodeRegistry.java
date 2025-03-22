package ru.vaschenko.ServiceDiscovery.registry;

import org.springframework.stereotype.Component;
import ru.vaschenko.ServiceDiscovery.dto.NodeRegisterDto;

import java.util.*;

@Component
public class NodeRegistry {
    private static String distributionNode;
    private static final LinkedHashMap<String, NodeInformation> nodes = new LinkedHashMap<>();
    private static Iterator<Map.Entry<String, NodeInformation>> iterator = nodes.entrySet().iterator();

    public static void setDistributionNodes(NodeRegisterDto nodeRegisterDto) {
        distributionNode = nodeRegisterDto.nodeUrl();
    }

    public static void addNode(NodeRegisterDto nodeRegisterDto) {
        nodes.put(nodeRegisterDto.nodeUrl(), new NodeInformation().setNodeUrl(nodeRegisterDto.nodeUrl()));
        resetIterator();
    }

    public static void removeNode(String nodeUrl) {
        nodes.remove(nodeUrl);
        resetIterator();
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

    public static NodeInformation getNextNode() {
        if (nodes.isEmpty()) {
            return null;
        }

        while (iterator.hasNext()) {
            NodeInformation node = iterator.next().getValue();
            if (node.getSubTaskRequest() == null) {
                return node;
            }
        }

        resetIterator();
        while (iterator.hasNext()) {
            NodeInformation node = iterator.next().getValue();
            if (node.getSubTaskRequest() == null) {
                return node;
            }
        }

        return null;
    }

    private static void resetIterator() {
        iterator = nodes.entrySet().iterator();
    }
}

