package ru.vaschenko.ServiceDiscovery.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
    public static final String BASE_URL = "serviceDiscovery/";
    public static final String NODE_M = BASE_URL + "nodeM/";
    public static final String NODE_REGISTER = "register";
    public static final String NODE_DETACH = "detach";

    public static final String TASK_M = BASE_URL + "taskM/";

    public static final String RETURN = "return";

    public static final String CLIENT_DISTRIBUTION_URL = "distributionNode/";
    public static final String CALCULATE_SQUARE = CLIENT_DISTRIBUTION_URL + "submit/task";
    public static final String COLLECT_RES = CLIENT_DISTRIBUTION_URL + "collect";
    public static final String CLEAR = CLIENT_DISTRIBUTION_URL + "clear";

    public static final String CLIENT_COMPUTING_URL = "computingNode/";
    public static final String CALCULATE_SUBTASK = CLIENT_COMPUTING_URL + "submit/subtask";
    public static final String PING = CLIENT_COMPUTING_URL + "ping";
}
