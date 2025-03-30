package ru.vaschenko.ServiceDiscovery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.ServiceDiscovery.dto.TaskRequest;
import ru.vaschenko.ServiceDiscovery.util.ApiPath;

import java.util.List;
import java.util.Map;

@FeignClient(value = "dist-node", url = "${client.distnode.url}")
public interface DistributionNodeClient {

  @PostMapping(value = ApiPath.CALCULATE_SQUARE)
  Map<String, Object> submitTask(@RequestBody TaskRequest taskRequest);

  @PostMapping(ApiPath.COLLECT_RES)
  List<Object> getCollectionRes(@RequestBody Map<String, Object> res);

  @PostMapping(ApiPath.CLEAR)
  void clearDistributor();
}
