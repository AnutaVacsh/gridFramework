package ru.vaschenko.ServiceDiscovery.services;

import feign.Feign;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vaschenko.ServiceDiscovery.client.ComputingNodeClient;
import ru.vaschenko.ServiceDiscovery.client.DistributionNodeClient;
import ru.vaschenko.ServiceDiscovery.dto.AnswerDto;
import ru.vaschenko.ServiceDiscovery.dto.FullTaskRequest;
import ru.vaschenko.ServiceDiscovery.dto.SubTaskRequest;
import ru.vaschenko.ServiceDiscovery.dto.TaskRequest;
import ru.vaschenko.ServiceDiscovery.registry.NodeInformation;
import ru.vaschenko.ServiceDiscovery.registry.NodeRegistry;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagementService {
    private final Feign.Builder feignBuilder;
    private final Object lock = new Object();

    private static final long POLL_INTERVAL_MS = 1000;
    private Deque<SubTaskRequest> subtaskQueue = new ArrayDeque<>();
    private List<Object> listResult = new ArrayList<>();
    private AtomicInteger activeTasks = new AtomicInteger(0);

    public List<Object> calculateTask(FullTaskRequest fullTaskRequest) {
        log.info("Начинаем решение задачи {}", fullTaskRequest);
        clearDistributor();
        activeTasks.set(0);

        Map<String, Object> subtask;
        SubTaskRequest str;

        while (true) {
            log.info("_______________________");
            if (NodeRegistry.getDistributionNodes() == null) {
                log.info("Распределительная нода не найдена!!!");
                break;
            }

            if (subtaskQueue.isEmpty()) {
                subtask = nextSubtask(new TaskRequest(fullTaskRequest.args(), fullTaskRequest.jarToDist()));
                log.info("Следующая подзадача {}", subtask);

                if (subtask == null) break;

                str = new SubTaskRequest(subtask, fullTaskRequest.jarToComp());
            } else {
                str = pollSubtaskQueue();
            }

            NodeInformation workNode = waitForAvailableNode();

            if (workNode == null) {
                log.error("Не удалось найти свободную ноду для выполнения задачи.");
                break;
            }

            workNode.setSubTaskRequest(str);
            log.info("Помечаем ноду как занятую");

            ComputingNodeClient client = getComputingNodeClientForHost(workNode.getNodeUrl());
            activeTasks.incrementAndGet();
            sendSubtaskAsync(client, str);

            log.info("Задачка отправлена, идём дальше...");

        }

        waitForResults();

        log.info("Возвращаем итог {}", listResult);
        return listResult;
    }

    private void waitForResults() {
        synchronized (lock) {
            while (activeTasks.get() > 0) {
                try {
                    log.info("Ждем завершения всех задач...");
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Ошибка ожидания завершения задач: {}", e.getMessage());
                }
            }
        }
    }

    private NodeInformation waitForAvailableNode() {
        log.info("Все ноды {}", NodeRegistry.getAllNodes().stream().toList());
        if (NodeRegistry.getAllNodes().isEmpty()) {
            log.info("Вычислительных нод нет(((");
            return null;
        }
        while (true) {
            NodeInformation workNode = NodeRegistry.getNextNode();

            if (workNode != null) {
                log.info("Свободная нода найдена - {}", workNode);
                return workNode;
            }

            try {
                log.info("Ждём освобождение ноды");
                Thread.sleep(POLL_INTERVAL_MS);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Ошибка при ожидании свободной ноды: {}", e.getMessage());
            }
        }
    }

    public void addNewRes(AnswerDto res) {
        log.info("Вернулся новый результат от ноды {}", res.url());
        NodeRegistry.getNode(res.url()).setSubTaskRequest(null);
        log.info("Освобождаем ноду");
        addRes(res.res());

        if (activeTasks.decrementAndGet() == 0) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    private void addRes(Map<String, Object> res) {
        log.info("Отправляем новый результат в метод collect()");
        listResult = (getNodeClientForHost(NodeRegistry.getDistributionNodes()).getCollectionRes(res));
        log.info("Положили актуальный результат");
    }

    public void sendSubtaskAsync(ComputingNodeClient client, SubTaskRequest str) {
        new Thread(() -> {
            try {
                client.calculate(str);
                log.info("Отправили задачку");
            } catch (Exception e) {
                log.error("Ошибка при отправке подзадачи: {}", e.getMessage());
            }
        }).start();
    }

    public Map<String, Object> nextSubtask(TaskRequest taskRequest) {
        return getNodeClientForHost(NodeRegistry.getDistributionNodes()).submitTask(taskRequest);
    }

    private void clearDistributor() {
        getNodeClientForHost(NodeRegistry.getDistributionNodes()).clearDistributor();
    }

    public ComputingNodeClient getComputingNodeClientForHost(String host) {
        return feignBuilder.target(ComputingNodeClient.class, host);
    }

    public DistributionNodeClient getNodeClientForHost(String host) {
        return feignBuilder.target(DistributionNodeClient.class, host);
    }

    public void pushSubtaskQueue(SubTaskRequest subTaskRequest) {
        subtaskQueue.addLast(subTaskRequest);
    }

    public SubTaskRequest pollSubtaskQueue() {
        return subtaskQueue.pollFirst();
    }
}
