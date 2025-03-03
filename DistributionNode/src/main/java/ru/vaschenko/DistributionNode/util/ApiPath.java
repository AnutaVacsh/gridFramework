package ru.vaschenko.DistributionNode.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
    public static final String BASE_URL = "distributionNode";
    public static final String CALCULATE_SQUARE = "submit/task";

    public static final String BASE_URL_CLIENT = "computingNode/";
    public static final String CALCULATE_SUBTASK = BASE_URL_CLIENT + "submit/subtask";
    public static final String PING = BASE_URL_CLIENT + "ping";
}
