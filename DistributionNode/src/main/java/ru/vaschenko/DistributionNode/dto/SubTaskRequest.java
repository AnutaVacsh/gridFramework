package ru.vaschenko.DistributionNode.dto;

import java.util.Map;

public record SubTaskRequest(
        Map<String, Object> args,
        byte[] jar
) { }
