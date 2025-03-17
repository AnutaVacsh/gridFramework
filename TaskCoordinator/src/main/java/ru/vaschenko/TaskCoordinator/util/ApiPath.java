package ru.vaschenko.TaskCoordinator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
    public static final String BASE_URL = "gridEnv";
    public static final String TASK_GET = "task/get";

    public static final String BASE_URL_CLIENT = "serviceDiscovery/";
    public static final String TASK_M = BASE_URL_CLIENT + "taskM/";
}
