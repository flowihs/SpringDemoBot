package org.springdemobot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdemobot.service.admin.AdminMessageHandlerService;
import org.springdemobot.enums.BotState;
import org.springdemobot.model.User;
import org.springdemobot.service.MessageService;
import org.springdemobot.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
@RequiredArgsConstructor
public class MessageController {
    private final UserService userService;
    private final AdminMessageHandlerService adminMessageHandlerService;
    private final MessageService messageService;
    private Map<Long, BotState> userStates = new ConcurrentHashMap<>();

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            User user = userService.registerUserIfNew(update.getMessage().getFrom().getId(), update.getMessage());

            if (user.isBlocked()) return;

            BotState currentState = userStates.getOrDefault(chatId, BotState.DEFAULT);

            switch (currentState) {
                case ADD_TO_BLACKLIST:
                    adminMessageHandlerService.processUsernameInput(messageText, chatId);
                    userStates.remove(chatId);
                    break;
                case DELETE_FROM_BLACKLIST:
                    adminMessageHandlerService.removeUserFromBlacklist(messageText, chatId);
                    userStates.remove(chatId);
                    break;
                default:
                    handler(user, chatId, messageText);
            }
        }
    }

    public void handler(User user, long chatId, String messageText) {
        switch (messageText) {
            case "/start":
                messageService.sendWelcomeMessage(chatId, user.getRole());
                break;
            case "/help":
                messageService.sendHelpMessage(chatId);
                break;
            case "Добавить в чёрный список":
                messageService.sendAddToBlacklistMessage(chatId, user);
                userStates.put(chatId, BotState.ADD_TO_BLACKLIST);
                break;
            case "Удалить из чёрного списка":
                messageService.sendDeleteFromBlacklistMessage(chatId, user);
                userStates.put(chatId, BotState.DELETE_FROM_BLACKLIST);
                break;
            default:
                messageService.sendDefaultMessage(chatId);
        }
    }
}