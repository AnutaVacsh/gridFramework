package ru.vaschenko.TaskCoordinator.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.TaskCoordinator.dto.FullTaskRequest;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.util.ApiPath;

@FeignClient(value = "service-discovery", url = "${client.sd.url}")
public interface ServiceDiscoveryClient {

  @PostMapping(value = ApiPath.TASK_M)
  List<ResultLatinSquare> submitTask(@RequestBody FullTaskRequest taskRequest);
}
