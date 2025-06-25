package org.springdemobot.enums;

public enum Role {
    ROLE_USER("Юзер"),
    ROLE_ADMIN("Админ");

    private final String role;

    Role(String text) {
        this.role = text;
    }

    public String get() {
        return role;
    }
}

