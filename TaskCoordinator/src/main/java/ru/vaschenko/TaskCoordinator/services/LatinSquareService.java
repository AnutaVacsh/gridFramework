package ru.vaschenko.TaskCoordinator.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.TaskCoordinator.client.DistributionNodeClintFacade;
import ru.vaschenko.TaskCoordinator.computation.DefaultCollector;
import ru.vaschenko.TaskCoordinator.computation.DefaultDistributor;
import ru.vaschenko.TaskCoordinator.computation.DefaultGenerator;
import ru.vaschenko.TaskCoordinator.computation.DefaultSolver;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.dto.SubTask;
import ru.vaschenko.TaskCoordinator.dto.Task;
import ru.vaschenko.TaskCoordinator.dto.TaskRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class LatinSquareService {

  private final DistributionNodeClintFacade client;
  private final JarPackingService jarPackingService;

  public List<ResultLatinSquare> solveTask(Task task) throws Exception {
    Map<String, Object> args = new HashMap<>();
    args.put("task", task);

    TaskRequest tr =
        new TaskRequest(
            args,
            jarPackingService.getJarBytes(
                List.of(
                    DefaultDistributor.class,
                    DefaultGenerator.class,
                    DefaultSolver.class,
                    DefaultCollector.class,
                    Task.class,
                    TaskRequest.class,
                    SubTask.class,
                    ResultLatinSquare.class)));

    log.debug("{}", tr);
    return client.submitTask(tr);
  }
}
