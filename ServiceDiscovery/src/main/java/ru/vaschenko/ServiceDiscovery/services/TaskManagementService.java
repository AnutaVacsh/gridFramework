package ru.vaschenko.ServiceDiscovery.services;

import feign.Feign;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.client.ComputingNodeClient;
import ru.vaschenko.ServiceDiscovery.client.DistributionNodeClient;
import ru.vaschenko.ServiceDiscovery.dto.AnswerDto;
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
  private List<Object> listResult = new ArrayList<>();

  public List<Object> calculateTask(FullTaskRequest fullTaskRequest) {
    log.info("Начинаем решение задачи {}", fullTaskRequest);
    clearDistributor();

    Map<String, Object> subtask;
    SubTaskRequest str;

    while(true) {
      log.info("_______________________");
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
      log.info("Помечаем ноду как занятую");

      ComputingNodeClient client = getComputingNodeClientForHost(workNode.getNodeUrl());
      sendSubtaskAsync(client, str);

      log.info("Задачка отправлена, идём дальше...");

    }
    log.info("Возвращаем итог {}", listResult);
    return listResult;
  }

  private NodeInformation waitForAvailableNode() {
    log.info("Все ноды {}", NodeRegistry.getAllNodes().stream().toList());
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
  public void addNewRes(AnswerDto res){
    log.info("Вернулся новый результат от ноды {}", res.url());

    NodeRegistry.getNode(res.url()).setSubTaskRequest(null);
    log.info("Освобождаем ноду");
    addRes(res.res());
  }

  private void addRes(Map<String, Object> res){
    listResult = (getNodeClientForHost(NodeRegistry.getDistributionNodes()).getCollectionRes(res));
    log.info("Положили актуальный результат");
  }

  @Scheduled(fixedRate = 60 * 1000)
  private boolean nodesFinishing(){
      for(NodeInformation inf: NodeRegistry.getAllNodes()){
        if(inf.getSubTaskRequest() != null) return false;
      }

      return true;
  }

  public void sendSubtaskAsync(ComputingNodeClient client, SubTaskRequest str) {
    new Thread(() -> {
      try {
        client.calculate(str);
        log.info("Отправили задачку");
      } catch (Exception e) {
        log.error("Ошибка при отправке подзадачи: {}", e.getMessage());
      }
    }).start();
  }
}
