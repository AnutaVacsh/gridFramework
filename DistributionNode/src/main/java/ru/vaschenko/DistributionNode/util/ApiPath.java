package ru.vaschenko.DistributionNode.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
    public static final String BASE_URL = "distributionNode";
    public static final String CALCULATE_SQUARE = "submit/task";
    public static final String CLEAR = "clear";

    public static final String BASE_URL_CLIENT = "computingNode/";
    public static final String CALCULATE_SUBTASK = BASE_URL_CLIENT + "submit/subtask";
    public static final String PING = BASE_URL_CLIENT + "ping";
    public static final String REGISTER = "register";
    public static final String DETACH = "detach";

    public static final String CLIENT_BASE_URL = "serviceDiscovery/";
    public static final String NODE_M = "nodeM/";
    public static final String NODE_REGISTER = CLIENT_BASE_URL + NODE_M + "register";
    public static final String NODE_DETACH = CLIENT_BASE_URL + NODE_M  + "detach";
}
