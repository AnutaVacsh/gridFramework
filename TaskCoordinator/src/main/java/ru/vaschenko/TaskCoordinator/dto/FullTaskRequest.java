package ru.vaschenko.TaskCoordinator.dto;

import java.util.Map;

public record FullTaskRequest(Map<String, Object> args, byte[] jarToDist, byte[] jarToComp) {}