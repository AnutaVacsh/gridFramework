package ru.vaschenko.ComputingNode.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.ComputingNode.dto.SubTaskRequest;
import ru.vaschenko.ComputingNode.util.ApiPath;

import java.util.List;
import java.util.Map;

@RequestMapping(ApiPath.BASE_URL)
public interface ComputingApi {
    @PostMapping(ApiPath.CALCULATE_SUBTASK)
    Map<String, Object> calculateLatinSquare(@RequestBody SubTaskRequest subTask);

    @GetMapping(ApiPath.PING)
    void ping();
}
