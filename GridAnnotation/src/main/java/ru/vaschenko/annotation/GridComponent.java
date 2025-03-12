package ru.vaschenko.annotation;

import ru.vaschenko.enams.TypeComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GridComponent {
    TypeComponent value();
}