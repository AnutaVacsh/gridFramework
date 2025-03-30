package ru.vaschenko.DistributionNode.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.DistributionNode.dto.NodeRegisterDto;
import ru.vaschenko.DistributionNode.services.DistributionServices;
import ru.vaschenko.DistributionNode.api.DistributionApi;
import ru.vaschenko.DistributionNode.dto.TaskRequest;
import ru.vaschenko.DistributionNode.services.NodeRegisterService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DistributionController implements DistributionApi {
    private final DistributionServices distributionServices;
    private final NodeRegisterService nodeRegisterService;

    @Override
    public Map<String, Object> getSubTask(TaskRequest task) {
        return distributionServices.distribute(task);
    }

    @Override
    public List<Object> getCollectionRes(Map<String, Object> res) {
        return distributionServices.collect(res);
    }

    @Override
    public void clearDistributor() {
        distributionServices.clear();
    }

    @Override
    public void nodeRegister(NodeRegisterDto nodeRegisterDto) {
        nodeRegisterService.nodeRegister(nodeRegisterDto);
    }

    @Override
    public void nodeDetach(String nodeUrl) {
        nodeRegisterService.nodeDetach(nodeUrl);
    }
}
