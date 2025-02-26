package ru.vaschenko.TaskCoordinator.dto;

public record SubTaskRequest(
        SubTask subTask,
        byte[] generator,
        byte[] solver
) { }
