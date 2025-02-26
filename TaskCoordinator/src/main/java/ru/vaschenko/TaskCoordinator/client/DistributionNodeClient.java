package ru.vaschenko.TaskCoordinator.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.dto.TaskRequest;
import ru.vaschenko.TaskCoordinator.util.ApiPath;

@FeignClient(value = "work-node", url = "${client.worknode.url}")
public interface DistributionNodeClient {

  @PostMapping(value = ApiPath.CALCULATE_SQUARE)
  List<ResultLatinSquare> submitTask(@RequestBody TaskRequest taskRequest);
}
