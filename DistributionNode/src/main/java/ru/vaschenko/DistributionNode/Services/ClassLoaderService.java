package ru.vaschenko.DistributionNode.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
@Service
public class ClassLoaderService {

    /**
     * Загружает все классы из JAR-файла.
     *
     * @param jarPath путь к JAR-файлу
     * @return список загруженных классов
     */
    public List<Class<?>> loadAllClasses(Path jarPath) {
        List<Class<?>> loadedClasses = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath.toFile());
             URLClassLoader loader = new URLClassLoader(new URL[]{jarPath.toUri().toURL()}, getClass().getClassLoader())) {

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                            .replace('/', '.')
                            .replace(".class", "");
                    try {
                        Class<?> loadedClass = loader.loadClass(className);
                        loadedClasses.add(loadedClass);
                        log.info("Класс {} загружен", className);
                    } catch (ClassNotFoundException e) {
                        log.warn("Не удалось загрузить класс {}", className, e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке JAR-файла: {}", jarPath, e);
            throw new RuntimeException("Не удалось загрузить классы из JAR", e);
        }
        return loadedClasses;
    }

    /**
     * Ищет класс, который реализует интерфейс, и вызывает единственный публичный метод с переданными параметрами.
     *
     * @param classes         список загруженных классов
     * @param interfaceName   имя интерфейса (без пакета)
     * @param parameterValues список параметров, которые нужно передать в метод
     * @return результат выполнения метода
     * @throws Exception при ошибках в процессе рефлексии
     */
    public Object findAndInvokeSinglePublicMethod(List<Class<?>> classes, String interfaceName, Object... parameterValues) throws Exception {
        // Находим интерфейс
        final Class<?> targetInterface = findInterface(classes, interfaceName);

        if (targetInterface == null) {
            throw new RuntimeException("Не найден интерфейс с именем " + interfaceName);
        }

        // Ищем класс, который реализует интерфейс
        Class<?> targetClass = findImplementingClass(classes, targetInterface);

        if (targetClass == null) {
            throw new RuntimeException("Не найден класс, реализующий интерфейс " + interfaceName);
        }

        // Получаем все публичные методы класса
        Method[] methods = targetClass.getDeclaredMethods();

        // Фильтруем только публичные методы
        Method targetMethod = Arrays.stream(methods)
                .filter(method -> method.getModifiers() == 1) // 1 - это модификатор публичного метода
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не найден публичный метод в классе " + targetClass.getName()));

        // Преобразуем параметры в нужные типы, используя рефлексию
        Class<?>[] parameterTypes = new Class[parameterValues.length];
        for (int i = 0; i < parameterValues.length; i++) {
            parameterTypes[i] = parameterValues[i].getClass();
        }

        // Вызываем метод на найденном классе
        return targetMethod.invoke(targetClass.getDeclaredConstructor().newInstance(), parameterValues);
    }

    /**
     * Ищет интерфейс среди загруженных классов
     */
    private Class<?> findInterface(List<Class<?>> classes, String interfaceName) {
        for (Class<?> clazz : classes) {
            for (Class<?> iface : clazz.getInterfaces()) {
                if (iface.getSimpleName().equals(interfaceName)) {
                    return iface;
                }
            }
        }
        return null;
    }

    /**
     * Ищет класс, реализующий указанный интерфейс
     */
    private Class<?> findImplementingClass(List<Class<?>> classes, Class<?> targetInterface) {
        for (Class<?> clazz : classes) {
            if (Arrays.stream(clazz.getInterfaces()).anyMatch(iface -> iface.equals(targetInterface))) {
                return clazz;
            }
        }
        return null;
    }
}



