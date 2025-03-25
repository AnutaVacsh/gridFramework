package ru.vaschenko.ComputingNode.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
    public static final String BASE_URL = "computingNode/";
    public static final String CALCULATE_SUBTASK = "submit/subtask";
    public static final String PING = "ping";
    public static final String REGISTER = "register";
    public static final String DETACH = "detach";

    public static final String CLIENT_BASE_URL = "serviceDiscovery/";
    public static final String NODE_M = "nodeM/";
    public static final String NODE_REGISTER = CLIENT_BASE_URL + NODE_M + "register";
    public static final String NODE_DETACH = CLIENT_BASE_URL + NODE_M  + "detach";
}
