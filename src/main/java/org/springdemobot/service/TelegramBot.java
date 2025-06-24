package org.springdemobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springdemobot.config.BotConfig;
import org.springdemobot.enums.BotMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UserService userService;
    private final MessageSenderService messageSender;
    private final BotCommandService botCommandService;

    @Autowired
    public TelegramBot(BotConfig config,
                       UserService userService,
                       MessageSenderService messageSender,
                       BotCommandService botCommandService) {
        this.config = config;
        this.userService = userService;
        this.messageSender = messageSender;
        this.botCommandService = botCommandService;
    }

    private void handleTextMessage(Update update) {
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        switch (messageText) {
            case "/start":
                userService.registerUserIfNew(update.getMessage());
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                break;
            case "/help":
                messageSender.sendMessage(chatId, BotMessage.HELP_MESSAGE);
                break;
            default:
                messageSender.sendMessage(chatId, BotMessage.UNKNOWN_COMMAND);
        }
    }

    private void startCommandReceived(long chatId, String name) {
        log.info("Replied to user: " + name);
        messageSender.sendMessage(chatId, BotMessage.WELCOME);
    }

    //|--------------------------------------------------------------------------------------------------------------------|
    @PostConstruct
    private void init() {
        botCommandService.initializeCommands(this);
        log.info("Bot initialized");
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleTextMessage(update);
            }
        } catch (Exception e) {
            log.error("Error processing update: {}", e.getMessage());
        }
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