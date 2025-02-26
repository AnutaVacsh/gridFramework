package ru.vaschenko.TaskCoordinator.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.List;

@Slf4j
@Service
public class CompilerServer {
  private static final String OUTPUT_DIR = "temp";

  public void compileJavaFiles(List<File> javaFiles) throws Exception {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

    Iterable<? extends JavaFileObject> compilationUnits =
        fileManager.getJavaFileObjectsFromFiles(javaFiles);
    boolean success =
        compiler
            .getTask(null, fileManager, null, List.of("-d", OUTPUT_DIR), null, compilationUnits)
            .call();
    fileManager.close();

    if (success) {
      log.info("Compilation successful!");
    } else {
      log.info("Compilation error(");
    }
  }
}
