package ru.vaschenko.ComputingNode.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.ComputingNode.dto.AnswerDto;
import ru.vaschenko.ComputingNode.dto.NodeRegisterDto;
import ru.vaschenko.ComputingNode.util.ApiPath;

import java.util.Map;

@FeignClient(value = "service-discovery", url = "${client.sd.url}")
public interface ServiceDiscoveryClient {

  @PostMapping(ApiPath.NODE_REGISTER)
  void nodeRegister(@RequestBody NodeRegisterDto nodeRegisterDto);

  @PostMapping(ApiPath.NODE_DETACH)
  void nodeDetach(String nodeUrl);

  @PostMapping(ApiPath.RETURN)
  void returnAnswer(AnswerDto res);
}
