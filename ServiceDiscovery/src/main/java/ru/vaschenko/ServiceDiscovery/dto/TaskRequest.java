package ru.vaschenko.ServiceDiscovery.dto;

import java.util.Map;

public record TaskRequest(Map<String, Object> args, byte[] jar) {}
