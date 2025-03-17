package ru.vaschenko.ServiceDiscovery.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.client.impl.ComputingNodeClintFacade;
import ru.vaschenko.ServiceDiscovery.client.impl.DistributionNodeClintFacade;
import ru.vaschenko.ServiceDiscovery.dto.FullTaskRequest;
import ru.vaschenko.ServiceDiscovery.dto.SubTaskRequest;
import ru.vaschenko.ServiceDiscovery.dto.TaskRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagementService {
  private final ComputingNodeClintFacade computingNodeClient;
  private final DistributionNodeClintFacade distributionNodeClint;

  public List<Object> submitTask(FullTaskRequest fullTaskRequest) {
    log.info("Начинаем решение задачи {}", fullTaskRequest);
    List<Map<String, Object>> listSubTask =
        distribute(new TaskRequest(fullTaskRequest.args(), fullTaskRequest.jarToDist()));

    List<Map<String, Object>> listRes = new ArrayList<>();

    for (Map<String, Object> sbt : listSubTask) {
      listRes.add(
          computingNodeClient.calculateLatinSquare(
              new SubTaskRequest(sbt, fullTaskRequest.jarToComp())));
    }

    //todo collect();
    return convertToList(listRes);
  }

  public List<Map<String, Object>> distribute(TaskRequest taskRequest) {
    return distributionNodeClint.submitTask(taskRequest);
  }

  private List<Object> convertToList(List<Map<String, Object>> nodeResults) {
    log.debug("Начало конвертации в список. Получены nodeResults: {}", nodeResults);
    String key = nodeResults.get(0).keySet().iterator().next();

    List<Object> allResults =
        nodeResults.stream()
            .map(map -> (List<Object>) map.values().iterator().next())
            .flatMap(List::stream)
            .toList();

    log.debug("Конвертированные результаты: {}", allResults);

    Map<String, Object> result = Map.of(key, allResults);
    log.debug("Возвращаемый результат: {}", result);

    return allResults;
//    return result;
  }
}
