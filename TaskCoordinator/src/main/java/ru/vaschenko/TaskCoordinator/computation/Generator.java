package ru.vaschenko.TaskCoordinator.computation;

import ru.vaschenko.TaskCoordinator.dto.SubTask;

public interface Generator<T, E> {
    T generate(E subTask);
}
