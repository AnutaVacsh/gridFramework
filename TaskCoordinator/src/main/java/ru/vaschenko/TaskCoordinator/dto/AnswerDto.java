package ru.vaschenko.TaskCoordinator.dto;

import java.util.Map;

public record AnswerDto(
        Map<String, Object> res,
        String url
) { }
