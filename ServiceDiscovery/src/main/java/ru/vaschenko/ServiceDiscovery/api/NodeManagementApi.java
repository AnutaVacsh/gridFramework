package ru.vaschenko.ServiceDiscovery.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.ServiceDiscovery.dto.NodeRegisterDto;
import ru.vaschenko.ServiceDiscovery.util.ApiPath;

@RequestMapping(ApiPath.NODE_M)
public interface NodeManagementApi {

    @PostMapping(ApiPath.NODE_REGISTER)
    void nodeRegister(@RequestBody NodeRegisterDto nodeRegisterDto);
}
