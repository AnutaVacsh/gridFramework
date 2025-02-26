package ru.vaschenko.TaskCoordinator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
    public static final String BASE_URL = "gridEnv";
    public static final String TASK_GET = "task/get";

    public static final String BASE_URL_CLIENT = "distributionNode/";
    public static final String CALCULATE_SQUARE = BASE_URL_CLIENT + "submit/task";
}
