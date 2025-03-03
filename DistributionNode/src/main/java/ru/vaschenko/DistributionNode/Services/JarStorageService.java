package ru.vaschenko.DistributionNode.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class JarStorageService {
    private static final String PROJECT_DIR = "target/jars";

    public Path saveJar(byte[] jarBytes) {
        try {
            Path dirPath = Paths.get(PROJECT_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path jarPath = dirPath.resolve("task-" + UUID.randomUUID() + ".jar");
            Files.write(jarPath, jarBytes);
            log.info("JAR-файл сохранен: {}", jarPath);
            return jarPath;
        } catch (IOException e) {
            log.error("Ошибка при сохранении JAR-файла", e);
            throw new RuntimeException("Не удалось сохранить JAR-файл", e);
        }
    }
}
