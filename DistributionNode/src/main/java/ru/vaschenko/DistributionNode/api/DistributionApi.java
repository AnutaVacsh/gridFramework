package ru.vaschenko.DistributionNode.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.DistributionNode.dto.NodeRegisterDto;
import ru.vaschenko.DistributionNode.dto.TaskRequest;
import ru.vaschenko.DistributionNode.util.ApiPath;

import java.util.List;
import java.util.Map;

@RequestMapping(ApiPath.BASE_URL)
public interface DistributionApi {

    @PostMapping(ApiPath.CALCULATE_SQUARE)
    Map<String, Object> getSubTask(@RequestBody TaskRequest task);

    @PostMapping(ApiPath.CLEAR)
    void clearDistributor();

    @PostMapping(ApiPath.REGISTER)
    void nodeRegister(@RequestBody NodeRegisterDto nodeRegisterDto);

    @PostMapping(ApiPath.DETACH)
    void nodeDetach(String nodeUrl);
}
