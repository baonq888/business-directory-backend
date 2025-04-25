package com.where.enums;

public enum JwtSecret {

    SECRET_KEY("ndfsdbfsabdsadbDNNDszndfmdsfdsfdsew");

    private final String key;

    JwtSecret(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
