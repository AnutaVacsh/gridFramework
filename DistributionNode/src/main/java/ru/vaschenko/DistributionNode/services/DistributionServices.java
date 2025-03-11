package ru.vaschenko.DistributionNode.services;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.DistributionNode.client.ComputingNodeClintFacade;
import ru.vaschenko.DistributionNode.dto.SubTaskRequest;
import ru.vaschenko.DistributionNode.dto.TaskRequest;
import ru.vaschenko.DistributionNode.enams.TypeComponent;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionServices {
  private final ComputingNodeClintFacade client;
  private final JarStorageService jarStorageService;
  private final ClassLoaderService jarClassLoaderService;

  private List<Class<?>> classes = new ArrayList<>();

  /** распределить задачи на подзадачи */
  public List<Object> distribute(TaskRequest task) {
    Path jarPath = jarStorageService.saveJar(task.jar());

    classes = jarClassLoaderService.loadAllClasses(jarPath);

    return sendToClient(distribution(task), task.jar());
  }

  private List<Object> sendToClient(List<Map<String, Object>> subtasks, byte[] jar) {
    List<Map<String, Object>> res = new ArrayList<>();

    for (Map<String, Object> s : subtasks) {
      res.add(client.calculateLatinSquare(new SubTaskRequest(s, jar)));
    }

    return collectResults(convertToList(res));
  }

  private List<Map<String, Object>> distribution(TaskRequest task) {
    try {
      return (List<Map<String, Object>>)
          jarClassLoaderService.findAndInvokeSinglePublicMethod(
              classes, TypeComponent.DISTRIBUTOR, task.args());
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при распределении задач", e);
    }
  }

  private List<Object> collectResults(Map<String, Object> results) {
    try {
      return (List<Object>)
          jarClassLoaderService.findAndInvokeSinglePublicMethod(
              classes, TypeComponent.COLLECTOR, results);
    } catch (Exception e) {
      log.error("Ошибка при соединении результатов", e);
      throw new RuntimeException("Ошибка при соединении результатов", e);
    }
  }

  private Map<String, Object> convertToList(List<Map<String, Object>> nodeResults) {
    String key = nodeResults.get(0).keySet().iterator().next();

    List<Object> allResults =
        nodeResults.stream()
            .map(map -> (List<Object>) map.values().iterator().next())
            .flatMap(List::stream)
            .toList();
    
    return Map.of(key, allResults);
  }
}
