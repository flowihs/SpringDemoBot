package org.springdemobot.enums;

import lombok.Getter;

@Getter
public enum BotMessage {
    HELP_MESSAGE("Вы можете выполнять команды в главном меню..."),
    WELCOME("Привет, рад встрече!"),
    UNKNOWN_COMMAND("Прости, я не знаю эту команду.");

    private final String text;

    BotMessage(String text) {
        this.text = text;
    }

    public String get() {
        return text;
    }
}