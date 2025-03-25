package ru.vaschenko.ServiceDiscovery.services;

import feign.Feign;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.client.ComputingNodeClient;
import ru.vaschenko.ServiceDiscovery.client.DistributionNodeClient;
import ru.vaschenko.ServiceDiscovery.dto.FullTaskRequest;
import ru.vaschenko.ServiceDiscovery.dto.SubTaskRequest;
import ru.vaschenko.ServiceDiscovery.dto.TaskRequest;
import ru.vaschenko.ServiceDiscovery.registry.NodeInformation;
import ru.vaschenko.ServiceDiscovery.registry.NodeRegistry;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagementService {
  private final Feign.Builder feignBuilder;

  private static final long POLL_INTERVAL_MS = 1000;
  private Deque<SubTaskRequest> subtaskQueue = new ArrayDeque<>();

  public List<Object> calculateTask(FullTaskRequest fullTaskRequest) {
    log.info("Начинаем решение задачи {}", fullTaskRequest);
    clearDistributor();

    Map<String, Object> subtask;
    SubTaskRequest str;
    List<Map<String, Object>> listRes = new ArrayList<>();

    while(true) {
      if(NodeRegistry.getDistributionNodes() == null){
        log.info("Распределительная нода не найдена!!!");
        break;
      }

      if(subtaskQueue.isEmpty()){
        subtask = nextSubtask(new TaskRequest(fullTaskRequest.args(), fullTaskRequest.jarToDist()));
        log.info("Следующая подзадача {}", subtask);

        if(subtask == null) break;

        str = new SubTaskRequest(subtask, fullTaskRequest.jarToComp());
      } else {
        str = pollSubtaskQueue();
      }

      NodeInformation workNode = waitForAvailableNode();

      if (workNode == null) {
        log.error("Не удалось найти свободную ноду для выполнения задачи.");
        break;
      }

      workNode.setSubTaskRequest(str);
      log.info("Новая подзадачка {}", str);

      ComputingNodeClient client = getComputingNodeClientForHost(workNode.getNodeUrl());
      Map<String, Object> res = client.calculate(str);
      log.info("Решение подзадачки {}", res);
      listRes.add(res);

      workNode.setSubTaskRequest(null);
    }

    return convertToList(listRes);
  }

  private NodeInformation waitForAvailableNode() {
    if (NodeRegistry.getAllNodes().isEmpty()){
      log.info("Вычислительных нод нет(((");
      return null;
    }
    while (true) {
      NodeInformation workNode = NodeRegistry.getNextNode();

      if (workNode != null) {
        log.info("Свободная нода найдена - {}", workNode);
        return workNode;
      }

      try {
        log.info("Ждём освобождение ноды");
        Thread.sleep(POLL_INTERVAL_MS);

      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        log.error("Ошибка при ожидании свободной ноды: {}", e.getMessage());
      }
    }
  }

  public Map<String, Object> nextSubtask(TaskRequest taskRequest) {
    return getNodeClientForHost(NodeRegistry.getDistributionNodes()).submitTask(taskRequest);
  }

  private void clearDistributor(){
    getNodeClientForHost(NodeRegistry.getDistributionNodes()).clearDistributor();
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
  }

  public ComputingNodeClient getComputingNodeClientForHost(String host) {
    return feignBuilder.target(ComputingNodeClient.class, host);
  }

  public DistributionNodeClient getNodeClientForHost(String host) {
    return feignBuilder.target(DistributionNodeClient.class, host);
  }

  public void pushSubtaskQueue(SubTaskRequest subTaskRequest){
    subtaskQueue.addLast(subTaskRequest);
  }

  public SubTaskRequest pollSubtaskQueue(){
    return subtaskQueue.pollFirst();
  }
}
