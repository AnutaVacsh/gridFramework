package ru.vaschenko.ComputingNode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.vaschenko.ComputingNode.dto.enam.NodeType;

public record NodeRegisterDto(
        @Schema(description = "Alphabet for the Latin square", example = "COMPUTING")
        NodeType type,
        @Schema(description = "Alphabet for the Latin square", example = "http://localhost:8084")
        String nodeUrl
) {
}
