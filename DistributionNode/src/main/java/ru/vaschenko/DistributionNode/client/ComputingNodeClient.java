package ru.vaschenko.DistributionNode.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.DistributionNode.dto.ResultLatinSquare;
import ru.vaschenko.DistributionNode.dto.SubTask;
import ru.vaschenko.DistributionNode.dto.SubTaskRequest;
import ru.vaschenko.DistributionNode.util.ApiPath;

import java.util.List;

@FeignClient(value = "work-node", url = "${client.worknode.url}")
public interface ComputingNodeClient {
    @PostMapping(ApiPath.CALCULATE_SUBTASK)
    List<ResultLatinSquare> calculateLatinSquare(@RequestBody SubTaskRequest subTask);

    @GetMapping(ApiPath.PING)
    void ping();
}
