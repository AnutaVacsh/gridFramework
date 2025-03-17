package ru.vaschenko.ServiceDiscovery.api;

import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.ServiceDiscovery.dto.FullTaskRequest;
import ru.vaschenko.ServiceDiscovery.util.ApiPath;

@RequestMapping(ApiPath.TASK_M)
public interface TaskManagementApi {

    @PostMapping
    List<Object> submitTask(@RequestBody FullTaskRequest taskRequest);
}
