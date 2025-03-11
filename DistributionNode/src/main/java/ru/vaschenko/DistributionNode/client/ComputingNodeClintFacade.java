package ru.vaschenko.DistributionNode.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.DistributionNode.annotation.FeignRetryable;
import ru.vaschenko.DistributionNode.dto.SubTaskRequest;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComputingNodeClintFacade implements ComputingNodeClient {
  private final ComputingNodeClient computingNodeClient;

  @Override
  @FeignRetryable
  public Map<String, Object> calculateLatinSquare(SubTaskRequest subTask) {
    try {
      log.info("Sending request for work node in subtask {}", subTask);
      Map<String, Object> result = computingNodeClient.calculateLatinSquare(subTask);
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
