package ru.vaschenko.ServiceDiscovery.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.annotation.FeignRetryable;
import ru.vaschenko.ServiceDiscovery.dto.SubTaskRequest;
import ru.vaschenko.ServiceDiscovery.client.ComputingNodeClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComputingNodeClintFacade implements ComputingNodeClient {
  private final ComputingNodeClient computingNodeClient;

  @Override
  @FeignRetryable
  public Map<String, Object> calculate(SubTaskRequest subTask) {
    try {
      log.info("Sending request for work node in subtask {}", subTask);
      Map<String, Object> result = computingNodeClient.calculate(subTask);
      log.info("Received result for subtask {}", subTask);
      return result;
    } catch (Exception e) {
      log.error(
              "Error occurred while calculating Latin Square for subtask {}: {}",
              subTask.args(),
              e.getMessage());
      throw new RuntimeException(
              "Error calculating Latin Square for subtask " + subTask.args(),
              e);
    }
  }

  @Override
  @FeignRetryable
  public void ping() {
    try {
      log.info("Sending ping to work node");
      computingNodeClient.ping();
      log.info("Ping successful");
    } catch (Exception e) {
      log.error("Error occurred while pinging work node: {}", e.getMessage());
      throw new RuntimeException("Error pinging work node", e);
    }
  }
}
