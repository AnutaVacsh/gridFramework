package ru.vaschenko.DistributionNode.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.DistributionNode.dto.NodeRegisterDto;
import ru.vaschenko.DistributionNode.util.ApiPath;

@FeignClient(value = "service-discovery", url = "${client.sd.url}")
public interface ServiceDiscoveryClient {

  @PostMapping(ApiPath.NODE_REGISTER)
  void nodeRegister(@RequestBody NodeRegisterDto nodeRegisterDto);

  @PostMapping(ApiPath.NODE_DETACH)
  void nodeDetach(String nodeUrl);
}
