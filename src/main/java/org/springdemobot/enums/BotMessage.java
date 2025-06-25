package org.springdemobot.enums;

import lombok.Getter;

@Getter
public enum BotMessage {
    HELP_MESSAGE("Вы можете выполнять команды в главном меню..."),
    WELCOME("Привет, рад встрече!"),
    UNKNOWN_COMMAND("Прости, я не знаю эту команду."),
    MUTED_FOR_PROFANITY("Ваше сообщение было удалено. Вы временно заглушены в чате за использование нецензурной лексики. Пожалуйста, соблюдайте правила.");

    private final String text;

    BotMessage(String text) {
        this.text = text;
    }

    public String get() {
        return text;
    }
}