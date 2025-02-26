package ru.vaschenko.TaskCoordinator.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.TaskCoordinator.client.DistributionNodeClintFacade;
import ru.vaschenko.TaskCoordinator.computation.Collector;
import ru.vaschenko.TaskCoordinator.computation.Distributor;
import ru.vaschenko.TaskCoordinator.computation.Generator;
import ru.vaschenko.TaskCoordinator.computation.Solver;
import ru.vaschenko.TaskCoordinator.dto.ResultLatinSquare;
import ru.vaschenko.TaskCoordinator.dto.SubTask;
import ru.vaschenko.TaskCoordinator.dto.Task;
import ru.vaschenko.TaskCoordinator.dto.TaskRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class LatinSquareService {

  private final DistributionNodeClintFacade client;
  private final CompilerServer compilerServer;
  private final JarPackingService jarPackingService;
  private final Solver solver;
  private final Generator generator;
  private final Distributor distributor;
  private final Collector collector;

  public List<ResultLatinSquare> solveTask(Task task) throws Exception {

    TaskRequest tr = new TaskRequest(
            task,
            jarPackingService.getJarBytes(
                    List.of(
                            distributor.getClass(),
                            generator.getClass(),
                            solver.getClass(),
                            collector.getClass(),
                            Task.class,
                            TaskRequest.class,
                            SubTask.class,
                            ResultLatinSquare.class)));

    log.debug("{}", tr);
    return client.submitTask(tr);

//    File distributorJavaFile = getJavaFilePath(distributor.getClass());
//    File generatorJavaFile = getJavaFilePath(generator.getClass());
//    File solverJavaFile = getJavaFilePath(solver.getClass());
//    File collectorJavaFile = getJavaFilePath(collector.getClass());
//
//    compilerServer.compileJavaFiles(
//        List.of(distributorJavaFile, generatorJavaFile, solverJavaFile, collectorJavaFile));
//
//    File distributorClassFile = getClassFilePath(distributor.getClass());
//    File generatorClassFile = getClassFilePath(generator.getClass());
//    File solverClassFile = getClassFilePath(solver.getClass());
//    File collectorClassFile = getClassFilePath(collector.getClass());
//
//    if (!generatorClassFile.exists()
//        || !solverClassFile.exists()
//        || !collectorClassFile.exists()
//        || !distributorClassFile.exists()) {
//      throw new FileNotFoundException("Class files not found after compilation!");
//    }
  }

//  private File getJavaFilePath(Class<?> clazz) throws IOException {
//    String className = clazz.getSimpleName() + ".java";
//    String packagePath = clazz.getPackageName().replace('.', '/');
//    return new File(new File("src/main/java/" + packagePath, className).getCanonicalPath());
//  }
//
//  private File getClassFilePath(Class<?> clazz) throws IOException {
//    String className = clazz.getSimpleName() + ".class";
//    String packagePath = clazz.getPackageName().replace('.', '/');
//    return new File(new File("temp/" + packagePath, className).getCanonicalPath());
//  }
}
