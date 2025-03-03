package ru.vaschenko.DistributionNode.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.DistributionNode.client.ComputingNodeClintFacade;
import ru.vaschenko.DistributionNode.dto.SubTaskRequest;
import ru.vaschenko.DistributionNode.dto.TaskRequest;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionServices {
    private final ComputingNodeClintFacade client;
    private final JarStorageService jarStorageService;
    private final ClassLoaderService jarClassLoaderService;

    private List<Class<?>> classes = new ArrayList<>();

    /**
     * распределить задачи на подзадачи
     */
    public List<Object> distribute(TaskRequest task) {
        Path jarPath = jarStorageService.saveJar(task.jar());

        classes = jarClassLoaderService.loadAllClasses(jarPath);

        try {
            return sendToClient(
                    (List<Object>) jarClassLoaderService.findAndInvokeSinglePublicMethod(
                            classes,
                            "Distributor",
                            task.task()
                    ),
                    task.jar()
            );

        } catch (Exception e) {
            log.error("Ошибка при распределении задач", e);
            throw new RuntimeException("Ошибка при распределении задач", e);
        }
    }

    private List<Object> sendToClient(List<Object> subtasks, byte[] jar) {
        List<List<?>> res = new ArrayList<>();

        for (Object s : subtasks) {
            res.add(client.calculateLatinSquare(new SubTaskRequest(s, jar)));
        }

        return collectResults(res);
    }

    private List<Object> collectResults(List<List<?>> results) {
        try {
            return (List<Object>) jarClassLoaderService.findAndInvokeSinglePublicMethod(classes, "Collector", results);
        } catch (Exception e) {
            log.error("Ошибка при соединении результатов", e);
            throw new RuntimeException("Ошибка при соединении результатов", e);
        }
    }
}
