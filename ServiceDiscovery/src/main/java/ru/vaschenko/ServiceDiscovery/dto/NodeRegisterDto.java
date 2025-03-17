package ru.vaschenko.ServiceDiscovery.dto;

import ru.vaschenko.ServiceDiscovery.dto.enam.NodeType;

public record NodeRegisterDto(
        NodeType type,
        String nodeUrl
) {
}
