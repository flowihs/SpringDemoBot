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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UserService userService;
    private final BotCommandService botCommandService;

    @Autowired
    public TelegramBot(BotConfig config,
                       UserService userService,
                       BotCommandService botCommandService) {
        this.config = config;
        this.userService = userService;
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
                sendMessage(chatId, BotMessage.HELP_MESSAGE);
                break;
            default:
                sendMessage(chatId, BotMessage.UNKNOWN_COMMAND);
        }
    }

    private void sendMessage(long chatId, BotMessage message) {
        sendMessage(chatId, message.get());
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage());
        }
    }

    private void startCommandReceived(long chatId, String name) {
        log.info("Replied to user: " + name);
        sendMessage(chatId, BotMessage.WELCOME);
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