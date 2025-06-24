package org.springdemobot.service;

import lombok.RequiredArgsConstructor;
import org.springdemobot.enums.BotMessage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service
@RequiredArgsConstructor
public class MessageHandlerService {
    private final UserService userService;
    private final MessageSenderService messageSenderService;

    public void handleUpdate(Update update, TelegramLongPollingBot bot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    userService.registerUserIfNew(update.getMessage());
                    messageSenderService.sendMessage(bot, chatId, BotMessage.WELCOME.get());
                    break;
                case "/help":
                    messageSenderService.sendMessage(bot, chatId, BotMessage.HELP_MESSAGE.get());
                    break;
                default:
                    messageSenderService.sendMessage(bot, chatId, BotMessage.UNKNOWN_COMMAND.get());
            }
        }
    }
}