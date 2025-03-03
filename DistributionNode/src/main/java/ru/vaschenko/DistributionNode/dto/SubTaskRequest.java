package ru.vaschenko.DistributionNode.dto;

public record SubTaskRequest(
        Object subTask,
        byte[] jar
) { }
