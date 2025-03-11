package ru.vaschenko.TaskCoordinator.computation;

import java.util.Collection;
import java.util.List;
import ru.vaschenko.TaskCoordinator.annotation.GridComponent;
import ru.vaschenko.TaskCoordinator.annotation.GridMethod;
import ru.vaschenko.TaskCoordinator.annotation.GridParam;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.enams.TypeComponent;

@GridComponent(TypeComponent.COLLECTOR)
public class DefaultCollector {

    @GridMethod
    public List<ResultLatinSquare> collect(@GridParam(name = "re") List<List<ResultLatinSquare>> re) {
        return re.stream()
                .flatMap(Collection::stream)
                .toList();
    }
}
