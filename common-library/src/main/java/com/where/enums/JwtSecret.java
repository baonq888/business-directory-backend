package com.where.enums;

public enum JwtSecret {

    SECRET_KEY("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXo0NTY3ODkwMTIzNDU2Nzg5MA==");

    private final String key;

    JwtSecret(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
