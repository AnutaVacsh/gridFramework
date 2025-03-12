package ru.vaschenko.ComputingNode.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.ComputingNode.dto.SubTaskRequest;
import ru.vaschenko.enams.TypeComponent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComputingService {
  private final JarStorageService jarStorageService;
  private final ClassLoaderService jarClassLoaderService;

  private List<Class<?>> classes = new ArrayList<>();

  public Map<String, Object> computingSubtask(SubTaskRequest subTask){
    Path pathJar = jarStorageService.saveJar(subTask.jar());
    classes = jarClassLoaderService.loadAllClasses(pathJar);

    Map<String, Object> gsbt = generate(subTask);
    log.debug("Востановленная подзадача {}", gsbt);

    //    Map<String, Object> ssbt = solve(gsbt);
    //    log.debug("Решённая подзадача {}", ssbt);

    return gsbt;
  }

  private Map<String, Object> generate(SubTaskRequest subTask) {
    try {
      return (Map<String, Object>)
          jarClassLoaderService.findAndInvokeSinglePublicMethod(
              classes, TypeComponent.GENERATOR, subTask.args());
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при генерации подзадач", e);
    }
  }

  private Map<String, Object> solve(Map<String, Object> arg) {
      try {
          return ( Map<String, Object>)
                  jarClassLoaderService.findAndInvokeSinglePublicMethod(
                          classes, TypeComponent.SOLVER, arg);
      } catch (Exception e) {
          throw new RuntimeException("Ошибка при решении задачи", e);
      }
  }
}
