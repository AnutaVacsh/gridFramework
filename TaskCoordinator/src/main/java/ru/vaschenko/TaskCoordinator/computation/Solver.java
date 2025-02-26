package ru.vaschenko.TaskCoordinator.computation;

public interface Solver<T, E, R> {
    T solve(E restoreSubTask, R subTask);
}
