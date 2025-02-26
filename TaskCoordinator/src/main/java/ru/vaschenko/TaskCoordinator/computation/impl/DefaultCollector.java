package ru.vaschenko.TaskCoordinator.computation.impl;

import org.springframework.stereotype.Service;
import ru.vaschenko.TaskCoordinator.computation.Collector;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;

import java.util.Collection;
import java.util.List;

@Service
public class DefaultCollector implements Collector<List<ResultLatinSquare>, List<List<ResultLatinSquare>>> {

    @Override
    public List<ResultLatinSquare> collect(List<List<ResultLatinSquare>> re) {
        return re.stream()
                .flatMap(Collection::stream)
                .toList();
    }
}
