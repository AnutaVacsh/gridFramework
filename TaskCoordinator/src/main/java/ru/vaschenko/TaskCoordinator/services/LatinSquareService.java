package ru.vaschenko.TaskCoordinator.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.TaskCoordinator.client.ServiceDiscoveryClintFacade;
import ru.vaschenko.TaskCoordinator.computation.DefaultCollector;
import ru.vaschenko.TaskCoordinator.computation.DefaultDistributor;
import ru.vaschenko.TaskCoordinator.computation.DefaultGenerator;
import ru.vaschenko.TaskCoordinator.computation.DefaultSolver;
import ru.vaschenko.TaskCoordinator.dto.FullTaskRequest;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.dto.SubTask;
import ru.vaschenko.TaskCoordinator.dto.Task;
import ru.vaschenko.TaskCoordinator.dto.TaskRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class LatinSquareService {

  private final ServiceDiscoveryClintFacade client;
  private final JarPackingService jarPackingService;

  public List<ResultLatinSquare> solveTask(Task task) throws Exception {
    Map<String, Object> args = new HashMap<>();
    args.put("task", task);

    byte[] jarToDist = jarPackingService.getJarBytes(
      List.of(
        DefaultDistributor.class,
        DefaultCollector.class,
        Task.class,
        TaskRequest.class,
        SubTask.class,
        ResultLatinSquare.class
      ));
    byte[] jarToComp = jarPackingService.getJarBytes(
      List.of(
        DefaultGenerator.class,
        DefaultSolver.class,
        SubTask.class,
        ResultLatinSquare.class
      ));

    FullTaskRequest tr =
        new FullTaskRequest(
            args, jarToDist, jarToComp);

    log.debug("{}", tr);
    return client.submitTask(tr);
  }
}
