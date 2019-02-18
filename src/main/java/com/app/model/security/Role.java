package com.app.model.security;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    SUPER("ROLE_SUPER");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
