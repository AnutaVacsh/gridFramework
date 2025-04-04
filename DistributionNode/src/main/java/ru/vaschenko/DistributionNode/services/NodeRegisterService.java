package ru.vaschenko.DistributionNode.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.DistributionNode.client.ServiceDiscoveryClient;
import ru.vaschenko.DistributionNode.dto.NodeRegisterDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class NodeRegisterService {
  private final ServiceDiscoveryClient client;

  public void nodeRegister(NodeRegisterDto nodeRegisterDto) {
    log.info("Запрос на регистрацию");
    client.nodeRegister(nodeRegisterDto);
  }

  public void nodeDetach(String nodeUrl) {
    log.info("Запрос на отсоединение");
    client.nodeDetach(nodeUrl);
  }
}
