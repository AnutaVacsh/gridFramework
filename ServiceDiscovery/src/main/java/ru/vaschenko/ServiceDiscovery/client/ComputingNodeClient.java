package ru.vaschenko.ServiceDiscovery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.ServiceDiscovery.dto.SubTaskRequest;
import ru.vaschenko.ServiceDiscovery.util.ApiPath;

import java.util.Map;

@FeignClient(value = "comp-node", url = "${client.compnode.url}")
public interface ComputingNodeClient {
    @PostMapping(ApiPath.CALCULATE_SUBTASK)
    void calculate(@RequestBody SubTaskRequest subTask);

    @GetMapping(ApiPath.PING)
    void ping();
}
