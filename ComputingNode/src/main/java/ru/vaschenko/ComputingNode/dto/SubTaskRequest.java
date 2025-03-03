package ru.vaschenko.ComputingNode.dto;

public record SubTaskRequest(
        Object subTask,
        byte[] jar
) { }
