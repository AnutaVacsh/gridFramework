package ru.vaschenko.TaskCoordinator.computation;

import java.io.IOException;

public interface Distributor<T, E> {
    T generationSubtasks(E task);
}
