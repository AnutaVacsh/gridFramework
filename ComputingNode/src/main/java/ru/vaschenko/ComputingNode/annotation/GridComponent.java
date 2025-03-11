package ru.vaschenko.ComputingNode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.vaschenko.ComputingNode.enams.TypeComponent;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GridComponent {
    TypeComponent value();
}