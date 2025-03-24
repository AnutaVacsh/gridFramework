package ru.vaschenko.DistributionNode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.vaschenko.DistributionNode.dto.enam.NodeType;

public record NodeRegisterDto(
        @Schema(description = "Alphabet for the Latin square", example = "DISTRIBUTION")
        NodeType type,
        @Schema(description = "Alphabet for the Latin square", example = "http://localhost:8081")
        String nodeUrl
) {
}
