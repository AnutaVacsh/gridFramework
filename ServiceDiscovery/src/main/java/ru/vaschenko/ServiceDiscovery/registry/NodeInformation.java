package ru.vaschenko.ServiceDiscovery.registry;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.vaschenko.ServiceDiscovery.dto.SubTaskRequest;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class NodeInformation {
    private String nodeUrl;
    private SubTaskRequest subTaskRequest;
    private LocalDateTime lastResponseTime;
}
