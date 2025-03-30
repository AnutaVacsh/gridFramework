package ru.vaschenko.ServiceDiscovery.dto;

import java.util.Map;

public record AnswerDto(
        Map<String, Object> res,
        String url
) { }
