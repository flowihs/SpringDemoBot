package org.springdemobot.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("Юзер"),
    ROLE_ADMIN("Админ"),
    ROLE_OWNER("Владелец");

    private final String role;

    Role(String text) {
        this.role = text;
    }
}

