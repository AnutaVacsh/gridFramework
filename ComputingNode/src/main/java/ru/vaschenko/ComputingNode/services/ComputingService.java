package ru.vaschenko.ComputingNode.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.ComputingNode.dto.SubTaskRequest;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComputingService {
  private final JarStorageService jarStorageService;
  private final ClassLoaderService jarClassLoaderService;

  private List<Class<?>> classes = new ArrayList<>();

  public List<Object> computingSubtask(SubTaskRequest subTask){
    Path pathJar = jarStorageService.saveJar(subTask.jar());
    classes = jarClassLoaderService.loadAllClasses(pathJar);

    List<List<Object>> gsbt = generate(subTask);
    log.debug("Востановленная подзадача {}", gsbt);

    List<Object> ssbt = solve(gsbt, subTask);
    log.debug("Решённая подзадача {}", ssbt);

    return ssbt;
  }

  private List<List<Object>> generate(SubTaskRequest subTask) {
    try {
      return (List<List<Object>>)
          jarClassLoaderService.findAndInvokeSinglePublicMethod(
              classes, "Generator", subTask.subTask());
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при генерации подзадач", e);
    }
  }

  private List<Object> solve(List<List<Object>> restore, SubTaskRequest subTask) {
      try {
          return (List<Object>)
                  jarClassLoaderService.findAndInvokeSinglePublicMethod(
                          classes, "Solver", restore, subTask.subTask());
      } catch (Exception e) {
          throw new RuntimeException("Ошибка при решении задачи", e);
      }
  }
}
