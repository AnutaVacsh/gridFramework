package ru.vaschenko.TaskCoordinator.dto;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public record SubTask(
        int treeLevel,
        BigInteger number,
        List<List<Character>> matrix,
        List<Character> alphabet
) { }