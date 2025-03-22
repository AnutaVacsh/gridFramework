package ru.vaschenko.ServiceDiscovery.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.ServiceDiscovery.api.NodeManagementApi;
import ru.vaschenko.ServiceDiscovery.dto.NodeRegisterDto;
import ru.vaschenko.ServiceDiscovery.services.NodeManagementService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NodeManagementController implements NodeManagementApi {
    private final NodeManagementService nodeManagementService;

    @Override
    public void nodeRegister(NodeRegisterDto nodeRegisterDto) {
        log.info("Регистрация ноды {}", nodeRegisterDto);
        nodeManagementService.nodeRegister(nodeRegisterDto);
    }

    @Override
    public void nodeDetach(String nodeUrl) {

    }
}
