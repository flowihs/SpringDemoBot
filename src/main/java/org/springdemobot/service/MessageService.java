package org.springdemobot.service;

import lombok.RequiredArgsConstructor;
import org.springdemobot.enums.BotMessage;
import org.springdemobot.enums.Role;
import org.springdemobot.model.User;
import org.springdemobot.service.admin.AdminMessageSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageService {
    @Lazy
    @Autowired
    private TelegramBot bot;
    private final AdminMessageSenderService adminMessageSenderService;

    public void sendWelcomeMessage(long chatId, Role role) {
        if (role.equals(Role.ROLE_USER)) {
            bot.sendMessage(chatId, BotMessage.WELCOME.get());
        } else if (role.equals(Role.ROLE_ADMIN)) {
            adminMessageSenderService.sendMessageAndKeyboard(chatId, BotMessage.WELCOME.get());
        }
    }

    public void sendHelpMessage(long chatId) {
        bot.sendMessage(chatId, BotMessage.HELP_MESSAGE.get());
    }

    public void sendAddToBlacklistMessage(long chatId, User user) {
        if (user.getRole().equals(Role.ROLE_USER)) return;
        bot.sendMessage(chatId, BotMessage.ADMIN_ADD_BLACKLIST_MESSAGE.get());
    }

    public void sendDeleteFromBlacklistMessage(long chatId, User user) {
        if (user.getRole().equals(Role.ROLE_USER)) return;
        bot.sendMessage(chatId, BotMessage.ADMIN_DELETE_FROM_BLACKLIST.get());
    }

    public void sendDefaultMessage(long chatId) {
        bot.sendMessage(chatId, BotMessage.UNKNOWN_COMMAND.get());
    }
}
