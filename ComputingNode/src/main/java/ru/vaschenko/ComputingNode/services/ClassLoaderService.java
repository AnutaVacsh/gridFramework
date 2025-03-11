package ru.vaschenko.ComputingNode.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.ComputingNode.annotation.GridComponent;
import ru.vaschenko.ComputingNode.annotation.GridMethod;
import ru.vaschenko.ComputingNode.annotation.GridParam;
import ru.vaschenko.ComputingNode.enams.TypeComponent;

@Slf4j
@Service
public class ClassLoaderService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Загружает все классы из JAR-файла.
     *
     * @param jarPath путь к JAR-файлу
     */
    public List<Class<?>> loadAllClasses(Path jarPath) {
        List<Class<?>> loadedClasses = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath.toFile());
             URLClassLoader loader = new URLClassLoader(new URL[]{jarPath.toUri().toURL()}, getClass().getClassLoader())) {

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                log.debug("Файл в jar, {}", entry);

                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                            .replace('/', '.')
                            .replace(".class", "");
                    log.debug("Новое имя, {}", className);
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
     * @param parameterValues   параметры
     * @param parameterValues список параметров, которые нужно передать в метод
     */
    public Object findAndInvokeSinglePublicMethod(
            List<Class<?>> classes,
            TypeComponent componentType,
            Object parameterValues) throws Exception {

        @SuppressWarnings("unchecked")
        Map<String, Object> paramsMap = (Map<String, Object>) parameterValues;

        Class<?> targetClass = classes.stream()
                .filter(cls -> cls.isAnnotationPresent(GridComponent.class))
                .filter(cls -> cls.getAnnotation(GridComponent.class).value().name().equals(componentType.name()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Не найден класс с GridComponent типа " + componentType));

        Method targetMethod = Arrays.stream(targetClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(GridMethod.class))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Не найден метод с GridMethod в классе " + targetClass.getName()));

        log.debug("Вызываем метод {} из класса {}", targetMethod.getName(), targetClass.getSimpleName());

        Parameter[] parameters = targetMethod.getParameters();
        Object[] castedParameters = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            GridParam annotation = parameters[i].getAnnotation(GridParam.class);
            if (annotation == null) {
                throw new RuntimeException("Все параметры метода должны быть помечены @GridParam");
            }

            String paramName = annotation.name();
            Object rawValue = paramsMap.get(paramName);
            if (rawValue == null) {
                throw new RuntimeException("Не найден параметр с именем " + paramName);
            }

            castedParameters[i] = objectMapper.convertValue(rawValue, parameters[i].getType());
        }

        Object instance = targetClass.getDeclaredConstructor().newInstance();
        return targetMethod.invoke(instance, castedParameters);
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



