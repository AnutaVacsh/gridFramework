package ru.vaschenko.DistributionNode.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.DistributionNode.client.ComputingNodeClintFacade;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionServices {
    private final ComputingNodeClintFacade client;

    /**
     * распределить задачи на подзадачи
     */
    public List<Object> distribute(Object task) {
        return null;
    }
}
