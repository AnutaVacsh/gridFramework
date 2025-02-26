package ru.vaschenko.TaskCoordinator.client;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.dto.TaskRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionNodeClintFacade implements DistributionNodeClient {
  private final DistributionNodeClient distributionNodeClient;

  @Override
  public List<ResultLatinSquare> submitTask(TaskRequest taskRequest) {
    try {
      log.info("Sending request for work node in task, {}", taskRequest);
      List<ResultLatinSquare> result = distributionNodeClient.submitTask(taskRequest);
      log.info("Received result for task");
      return result;
    } catch (Exception e) {
      log.error(
              "Error occurred while calculating Latin Square for task: {}",
              e.getMessage());
      throw new RuntimeException(
              "Error calculating Latin Square for task", e);
    }
  }
}
