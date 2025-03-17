package ru.vaschenko.ServiceDiscovery.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.ServiceDiscovery.api.TaskManagementApi;
import ru.vaschenko.ServiceDiscovery.dto.FullTaskRequest;
import ru.vaschenko.ServiceDiscovery.services.TaskManagementService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskManagementController implements TaskManagementApi {
    private final TaskManagementService taskManagementService;

    @Override
    public List<Object> submitTask(FullTaskRequest taskRequest) {
        log.info("Пришла задачка {}", taskRequest);
        return taskManagementService.submitTask(taskRequest);
    }
}
