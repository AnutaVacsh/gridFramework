package ru.vaschenko.DistributionNode.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DynamicClassLoader extends ClassLoader{
    public Class<?> loadClassFromBytes(byte[] classBytes) {
        log.info("loading class");
        return defineClass(null, classBytes, 0, classBytes.length);
    }
}
