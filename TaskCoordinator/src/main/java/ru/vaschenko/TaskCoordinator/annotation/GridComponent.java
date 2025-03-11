package ru.vaschenko.TaskCoordinator.annotation;

import ru.vaschenko.TaskCoordinator.enams.TypeComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GridComponent {
    TypeComponent value();
}