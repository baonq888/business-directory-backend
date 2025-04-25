package com.where.e2e_tests.utils;

import java.util.HashMap;
import java.util.Map;

public class TokenContext {
    private static final Map<String, String> tokens = new HashMap<>();

    public static void add(String key, String token) {
        tokens.put(key, token);
    }

    public static String get(String key) {
        return tokens.get(key);
    }

}
