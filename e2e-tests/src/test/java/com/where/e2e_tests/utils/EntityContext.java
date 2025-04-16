package com.where.e2e_tests.utils;

import java.util.HashMap;
import java.util.Map;

public class EntityContext {
    private static final Map<String, Object> store = new HashMap<>();

    // Save entity with a custom key
    public static <T> void put(String key, T value) {
        store.put(key, value);
    }

    // Get entity by key
    public static <T> T get(String key) {
        return (T) store.get(key);
    }

    // Remove specific entity by key
    public static void remove(String key) {
        store.remove(key);
    }

    // Clear everything
    public static void clear() {
        store.clear();
    }
}