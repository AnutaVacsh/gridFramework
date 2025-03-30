package ru.vaschenko.TaskCoordinator.computation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.annotation.GridComponent;
import ru.vaschenko.annotation.GridMethod;
import ru.vaschenko.annotation.GridParam;
import ru.vaschenko.enams.TypeComponent;

@GridComponent(TypeComponent.COLLECTOR)
public class DefaultCollector {
    private static final List<ResultLatinSquare> results = new ArrayList<>();

    @GridMethod
    public List<ResultLatinSquare> collect(@GridParam(name = "re") List<ResultLatinSquare> re) {
        results.addAll(re);
        return results;
    }

    public synchronized void clear() {
        results.clear();
    }
}
