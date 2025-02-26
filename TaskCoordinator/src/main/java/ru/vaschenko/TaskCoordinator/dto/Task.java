package ru.vaschenko.TaskCoordinator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Schema(description = "Represents a task for calculating a Latin square")
public record Task(
    @Schema(description = "Alphabet for the Latin square", example = "[\"A\", \"B\", \"C\", \"D\"]")
        List<Character> alphabet,
    @Schema(
            description = "Matrix representing the Latin square (some values may be null)",
            example =
                "[[\"A\", null, null, null], [null, \"A\", null, \"C\"], [\"C\", null, \"A\", null], [null, \"C\", null, \"A\"]]")
        List<List<Character>> matrix) implements Serializable {}
