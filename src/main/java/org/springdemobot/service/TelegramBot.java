package org.springdemobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springdemobot.service.admin.AdminMessageHandlerService;
import org.springdemobot.config.BotConfig;
import org.springdemobot.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final MessageController messageHandlerService;
    private final BotCommandService botCommandService;


    @Autowired
    public TelegramBot(BotConfig config,
                       MessageController messageHandlerService,
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
        messageHandlerService.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage());
        }
    }
}