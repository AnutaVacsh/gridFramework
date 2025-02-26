package ru.vaschenko.TaskCoordinator.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.jar.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JarPackingService {
  private final String classesDirPath = "target/classes";

  public byte[] getJarBytes(List<Class<?>> includedClasses) throws IOException {
    log.info("==> Начинаем упаковку JAR-файла...");
    log.info("Всего классов для упаковки: {}", includedClasses.size());

    List<String> classPaths = includedClasses.stream()
            .map(this::getClassFilePath)
            .collect(Collectors.toList());

    log.info("Файлы для упаковки:");
    classPaths.forEach(path -> log.info("  - {}", path));

    Path tempJar = Files.createTempFile("classes-", ".jar");
    log.info("Временный JAR-файл создан: {}", tempJar);

    createJar(tempJar.toString(), classPaths);
    byte[] jarBytes = Files.readAllBytes(tempJar);

    log.info("JAR-файл успешно создан, размер: {} байт", jarBytes.length);

    Files.delete(tempJar);
    log.info("Временный JAR-файл удалён");

    return jarBytes;
  }

  private String getClassFilePath(Class<?> clazz) {
    return clazz.getName().replace('.', '/') + ".class";
  }

  private void createJar(String jarFilePath, List<String> includedClasses) throws IOException {
    try (JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(jarFilePath))) {
      for (String classFile : includedClasses) {
        Path classPath = Paths.get(classesDirPath, classFile);
        if (Files.exists(classPath)) {
          addFileToJar(jarOut, classPath, classFile);
        } else {
          log.warn("Файл {} не найден и не будет добавлен в JAR", classFile);
        }
      }
    }
  }

  private void addFileToJar(JarOutputStream jarOut, Path filePath, String entryName) throws IOException {
    try (InputStream in = Files.newInputStream(filePath)) {
      JarEntry entry = new JarEntry(entryName);
      jarOut.putNextEntry(entry);
      in.transferTo(jarOut);
      jarOut.closeEntry();
      log.debug("Добавлен файл {} в JAR", entryName);
    }
  }
}
