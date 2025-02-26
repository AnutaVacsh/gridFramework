package ru.vaschenko.TaskCoordinator.computation;

public interface Collector<T, E> {
    T collect(E response);
}
