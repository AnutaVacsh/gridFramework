package ru.vaschenko.DistributionNode.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vaschenko.DistributionNode.util.ApiPath;

import java.util.List;

@RequestMapping(ApiPath.BASE_URL)
public interface DistributionApi {

    @PostMapping(ApiPath.CALCULATE_SQUARE)
    public List<Object> getSubTask(@RequestBody Object task);
}
