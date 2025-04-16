package com.where.e2e_tests.utils;

import java.util.HashMap;
import java.util.Map;

public class EntityContext {
    private static final Map<Class<?>, Object> store = new HashMap<>();

    // Save entity
    public static <T> void put(Class<T> clazz, T value) {
        store.put(clazz, value);
    }

    // Get entity
    public static <T> T get(Class<T> clazz) {
        return clazz.cast(store.get(clazz));
    }

    // Remove specific entity
    public static <T> void remove(Class<T> clazz) {
        store.remove(clazz);
    }

    // Clear everything
    public static void clear() {
        store.clear();
    }
}