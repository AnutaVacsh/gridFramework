package ru.vaschenko.DistributionNode.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.DistributionNode.Services.DistributionServices;
import ru.vaschenko.DistributionNode.api.DistributionApi;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class DistributionController implements DistributionApi {
    private final DistributionServices distributionServices;

    @Override
    public List<Object> getSubTask(Object task) {
        return distributionServices.distribute(task);
    }
}
