package ru.vaschenko.ServiceDiscovery.registry;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class NodeInformation {
    private String nodeUrl;
    private int taskId;
    private LocalDateTime lastResponseTime;
}
