package org.springdemobot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdemobot.admin.AdminMessageHandlerService;
import org.springdemobot.admin.AdminMessageSenderService;
import org.springdemobot.enums.BotMessage;
import org.springdemobot.enums.BotState;
import org.springdemobot.enums.Role;
import org.springdemobot.model.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.awt.SystemColor.text;


@Service
@Slf4j
@RequiredArgsConstructor
public class MessageHandlerService {
    private final UserService userService;
    private final MessageSenderService messageSenderService;
    private final AdminMessageSenderService adminMessageSenderService;
    private final AdminMessageHandlerService adminMessageHandlerService;
    private Map<Long, BotState> userStates = new ConcurrentHashMap<>();


    public void handleUpdate(Update update, TelegramLongPollingBot bot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            User user = userService.registerUserIfNew(update, update.getMessage());

            if (user.isBlocked()) return;

            BotState currentState = userStates.getOrDefault(chatId, BotState.DEFAULT);

            switch (currentState) {
                case ADD_TO_BLACKLIST:
                    adminMessageHandlerService.processUsernameInput(bot, messageText, chatId);
                    userStates.remove(chatId);
                    break;
                case DELETE_FROM_BLACKLIST:
                    adminMessageHandlerService.removeUserFromBlacklist(bot, messageText, chatId);
                    userStates.remove(chatId);
                    break;
                default:
                    handler(update, bot, user, chatId, messageText);
            }
        }
    }

    public void handler(Update update, TelegramLongPollingBot bot, User user, long chatId, String messageText) {
        switch (messageText) {
            case "/start":
                if (user.getRole().equals(Role.ROLE_USER)) {
                    messageSenderService.sendMessage(bot, chatId, BotMessage.WELCOME.get());
                }
                if (user.getRole().equals(Role.ROLE_ADMIN)) {
                    adminMessageSenderService.sendMessage(bot, update, chatId, BotMessage.WELCOME.get());
                }
                break;
            case "/help":
                messageSenderService.sendMessage(bot, chatId, BotMessage.HELP_MESSAGE.get());
                break;
            case "Добавить в чёрный список":
                if (user.getRole().equals(Role.ROLE_USER)) break;
                messageSenderService.sendMessage(bot, chatId, BotMessage.ADMIN_ADD_BLACKLIST_MESSAGE.get());
                userStates.put(chatId, BotState.ADD_TO_BLACKLIST);
                break;
            case "Удалить из чёрного списка":
                if (user.getRole().equals(Role.ROLE_USER)) break;
                messageSenderService.sendMessage(bot, chatId, BotMessage.ADMIN_DELETE_FROM_BLACKLIST.get());
                userStates.put(chatId, BotState.DELETE_FROM_BLACKLIST);
                break;
            default:
                messageSenderService.sendMessage(bot, chatId, BotMessage.UNKNOWN_COMMAND.get());
        }
    }
}