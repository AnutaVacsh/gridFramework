package ru.vaschenko.DistributionNode.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.DistributionNode.services.DistributionServices;
import ru.vaschenko.DistributionNode.api.DistributionApi;
import ru.vaschenko.DistributionNode.dto.TaskRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DistributionController implements DistributionApi {
    private final DistributionServices distributionServices;

    @Override
    public List<Map<String, Object>> getSubTask(TaskRequest task) {
        return distributionServices.distribute(task);
    }
}
