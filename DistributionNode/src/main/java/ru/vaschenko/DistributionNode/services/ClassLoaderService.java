package ru.vaschenko.DistributionNode.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.DistributionNode.enams.TypeComponent;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
        URLClassLoader loader =
            new URLClassLoader(new URL[] {jarPath.toUri().toURL()}, getClass().getClassLoader())) {

      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();

        if (entry.getName().endsWith(".class")) {
          String className = entry.getName().replace('/', '.').replace(".class", "");
          try {
            Class<?> loadedClass = loader.loadClass(className);
            loadedClasses.add(loadedClass);
            log.info("Класс {} загружен с ClassLoader: {}", loadedClass.getName(), loadedClass.getClassLoader());
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
   * Ищет класс c аннотацией @GridComponent(componentType), и вызывает публичный метод с
   * аннотацией @GridMethod с переданными параметрами.
   *
   * @param loadedClasses список загруженных классов
   * @param componentType имя нужного класса
   * @param parameterValues список параметров, которые нужно передать в метод
   */
  public Object findAndInvokeSinglePublicMethod(
          List<Class<?>> loadedClasses, // Список классов, загруженных через ClassLoader
          TypeComponent componentType,
          Object parameterValues) throws Exception {

    @SuppressWarnings("unchecked")
    Map<String, Object> paramsMap = (Map<String, Object>) parameterValues;

    // Загружаем аннотацию по имени, если она передается
    Class<? extends Annotation> gridComponentAnnotation = findAnnotationBySimpleName(loadedClasses, "GridComponent");

    // Ищем класс, который содержит нужную аннотацию
    Class<?> targetClass = loadedClasses.stream()
            .filter(cls -> cls.isAnnotationPresent(gridComponentAnnotation))
            .filter(cls -> cls.getAnnotation(gridComponentAnnotation).value().name().equals(componentType.name()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Не найден класс с GridComponent типа " + componentType));

    // Ищем метод с аннотацией GridMethod в найденном классе
    Class<? extends Annotation> gridMethodAnnotation = findAnnotationBySimpleName(loadedClasses, "GridMethod");
    Method targetMethod = Arrays.stream(targetClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(gridMethodAnnotation))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Не найден метод с GridMethod в классе " + targetClass.getName()));

    log.debug("Вызываем метод {} из класса {}", targetMethod.getName(), targetClass.getSimpleName());

    // Получаем параметры метода и подготавливаем их
    Parameter[] parameters = targetMethod.getParameters();
    Object[] castedParameters = new Object[parameters.length];

    for (int i = 0; i < parameters.length; i++) {
      // Получаем аннотацию GridParam на параметре
      GridParam gridParam = parameters[i].getAnnotation(GridParam.class);
      if (gridParam == null) {
        throw new RuntimeException("Все параметры метода должны быть помечены @GridParam");
      }
      String paramName = gridParam.name();
      Object rawValue = paramsMap.get(paramName);
      if (rawValue == null) {
        throw new RuntimeException("Не найден параметр с именем " + paramName);
      }
      castedParameters[i] = objectMapper.convertValue(rawValue, parameters[i].getType());
    }

    // Создаем экземпляр класса и вызываем метод
    Object instance = targetClass.getDeclaredConstructor().newInstance();
    return targetMethod.invoke(instance, castedParameters);
  }

  private Class<? extends Annotation> findAnnotationBySimpleName(List<Class<?>> loadedClasses, String annotationName) {
    return loadedClasses.stream()
            .flatMap(cls -> Arrays.stream(cls.getDeclaredAnnotations()))
            .filter(annotation -> annotation.annotationType().getSimpleName().equals(annotationName))
            .map(Annotation::annotationType)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Не найдена аннотация " + annotationName));
  }



  private List<Class<?>> findClassWithAnnotationByName(String annotationName, List<Class<?>> loadedClasses) {
    List<Class<?>> matchedClasses = new ArrayList<>();

    for (Class<?> loadedClass : loadedClasses) {
      Annotation[] annotations = loadedClass.getAnnotations();

      for (Annotation annotation : annotations) {
        if (annotation.annotationType().getSimpleName().equals(annotationName)) {
          matchedClasses.add(loadedClass);
          break;
        }
      }
    }
    return matchedClasses;
  }



  /** Ищет интерфейс среди загруженных классов */
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

  /** Ищет класс, реализующий указанный интерфейс */
  private Class<?> findImplementingClass(List<Class<?>> classes, Class<?> targetInterface) {
    for (Class<?> clazz : classes) {
      if (Arrays.stream(clazz.getInterfaces()).anyMatch(iface -> iface.equals(targetInterface))) {
        return clazz;
      }
    }
    return null;
  }
}
