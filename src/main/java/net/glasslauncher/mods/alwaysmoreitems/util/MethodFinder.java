package net.glasslauncher.mods.alwaysmoreitems.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodFinder {
    // Don't worry about it :)
    private static final Cache<Class<?>, Cache<Class<? extends Annotation>, Method>> CACHE = Caffeine.newBuilder().build();

    public static Method findMethodWithAnnotation(Class<?> class_, Class<? extends Annotation> annotation) {
        return CACHE.get(class_, c -> Caffeine.newBuilder().build()).get(annotation,
        a -> {
            Method[] methods = class_.getMethods();
            Method foundMethod = null;
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotation)) {
                    foundMethod = method;
                    break;
                }
            }
            return foundMethod;
        });
    }
}
