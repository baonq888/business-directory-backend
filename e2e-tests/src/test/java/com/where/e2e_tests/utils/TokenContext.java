package com.where.e2e_tests.utils;

import java.util.HashMap;
import java.util.Map;

public class TokenContext {
    private static final Map<String, String> tokens = new HashMap<>();

    public static void put(String key, String token) {
        tokens.put(key, token);
    }

    public static String get(String key) {
        return tokens.get(key);
    }

    public static void remove(String key) {
        tokens.remove(key);
    }

    public static void clear() {
        tokens.clear();
    }
}
