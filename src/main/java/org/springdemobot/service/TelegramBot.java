package org.springdemobot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdemobot.config.BotConfig;
import org.springdemobot.enums.BotMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final MessageHandlerService messageHandlerService;
    private final BotCommandService botCommandService;

    @Autowired
    public TelegramBot(BotConfig config,
                       MessageHandlerService messageHandlerService,
                       BotCommandService botCommandService) {
        this.config = config;
        this.messageHandlerService = messageHandlerService;
        this.botCommandService = botCommandService;
    }

    @PostConstruct
    private void init() {
        botCommandService.initializeCommands(this);
        log.info("Bot initialized");
    }

    @Override
    public void onUpdateReceived(Update update) {
        messageHandlerService.handleUpdate(update, this);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }
}