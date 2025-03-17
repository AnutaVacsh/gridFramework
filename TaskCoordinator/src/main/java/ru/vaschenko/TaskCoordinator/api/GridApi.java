package ru.vaschenko.TaskCoordinator.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.dto.Task;
import ru.vaschenko.TaskCoordinator.util.ApiPath;

import java.util.List;

@RequestMapping(ApiPath.BASE_URL)
public interface GridApi {

    @PostMapping(ApiPath.TASK_GET)
    public List<ResultLatinSquare> solveTask(@RequestBody Task task) throws Exception;
}