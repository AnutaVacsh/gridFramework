package ru.vaschenko.ComputingNode.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.ComputingNode.api.ComputingApi;
import ru.vaschenko.ComputingNode.dto.NodeRegisterDto;
import ru.vaschenko.ComputingNode.dto.SubTaskRequest;
import ru.vaschenko.ComputingNode.services.ComputingService;
import ru.vaschenko.ComputingNode.services.NodeRegisterService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ComputingController implements ComputingApi {
    private final ComputingService computingService;
    private final NodeRegisterService nodeRegisterService;

    @Override
    public Map<String, Object> calculateLatinSquare(SubTaskRequest subTask) {
        return computingService.computingSubtask(subTask);
    }

    @Override
    public void ping() {

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
