package ru.vaschenko.DistributionNode.services;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.DistributionNode.dto.TaskRequest;
import ru.vaschenko.DistributionNode.enams.TypeComponent;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionServices {
  private final JarStorageService jarStorageService;
  private final ClassLoaderService jarClassLoaderService;

  private List<Class<?>> classes = new ArrayList<>();

  /** распределить задачи на подзадачи */
  public Map<String, Object> distribute(TaskRequest task) {
    Path jarPath = jarStorageService.saveJar(task.jar());

    if (classes.isEmpty()) classes = jarClassLoaderService.loadAllClasses(jarPath);

    return distribution(task);
  }

  private Map<String, Object> distribution(TaskRequest task) {
    try {
      return (Map<String, Object>)
          jarClassLoaderService.findAndInvokeSinglePublicMethod(
              classes, TypeComponent.DISTRIBUTOR, task.args());
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при распределении задач", e);
    }
  }

  public void clear(){
    classes.clear();
  }
}