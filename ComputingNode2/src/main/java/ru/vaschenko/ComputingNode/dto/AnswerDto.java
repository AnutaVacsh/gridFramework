package ru.vaschenko.ComputingNode.dto;

import java.util.Map;

public record AnswerDto(
        Map<String, Object> res,
        String url
) { }
