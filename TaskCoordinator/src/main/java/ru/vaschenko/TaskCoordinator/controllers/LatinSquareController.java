package ru.vaschenko.TaskCoordinator.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.TaskCoordinator.api.GridApi;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.dto.Task;
import ru.vaschenko.TaskCoordinator.services.LatinSquareService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LatinSquareController implements GridApi {
    private final LatinSquareService latinSquareService;

    @Override
    public List<ResultLatinSquare> solveTask(Task task) throws Exception {
        return latinSquareService.solveTask(task);
    }
}
