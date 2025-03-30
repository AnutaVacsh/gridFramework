package ru.vaschenko.ServiceDiscovery.client.impl;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.annotation.FeignRetryable;
import ru.vaschenko.ServiceDiscovery.client.DistributionNodeClient;
import ru.vaschenko.ServiceDiscovery.dto.TaskRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionNodeClintFacade implements DistributionNodeClient {
  private final DistributionNodeClient distributionNodeClient;

  @Override
  @FeignRetryable
  public Map<String, Object> submitTask(TaskRequest taskRequest) {
    try {
      log.info("Sending request for work node in task, {}", taskRequest);
      Map<String, Object> result = distributionNodeClient.submitTask(taskRequest);
      log.info("Received result for task");
      return result;
    } catch (Exception e) {
      log.error("Error occurred while calculating Latin Square for task: {}", e.getMessage());
      throw new RuntimeException("Error calculating Latin Square for task", e);
    }
  }

  @Override
  public List<Object> getCollectionRes(Map<String, Object> res) {
    return List.of();
  }

  @Override
  public void clearDistributor() {
    try {
      log.info("Sending request for dist node for clear classes");
      distributionNodeClient.clearDistributor();
      log.info("Received result for task");
    } catch (Exception e) {
      log.error("Error occurred while dist node for clear classes", e.getMessage());
      throw new RuntimeException("Error clear classes for dist node", e);
    }
  }
}
