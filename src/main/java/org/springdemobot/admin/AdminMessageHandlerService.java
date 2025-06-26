package org.springdemobot.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdemobot.enums.BotMessage;
import org.springdemobot.enums.Role;
import org.springdemobot.model.User;
import org.springdemobot.repository.UserRepository;
import org.springdemobot.service.MessageHandlerService;
import org.springdemobot.service.MessageSenderService;
import org.springdemobot.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMessageHandlerService {
    private final UserRepository userRepository;
    private final MessageSenderService messageService;
    private final static String IncorrectData = "Вы ввели некорректные данные!";
    private final static String UserNotFound = "Вы ввели не существующего пользователя!";

    public void processUsernameInput(TelegramLongPollingBot bot, String messageText, long chatId) {
        if (!messageText.startsWith("@")) {
            messageService.sendMessage(bot, chatId, IncorrectData);
            return;
        }
        String userNameToBlock = messageText.replace("@", "");
        Optional<User> user = userRepository.findByUserName(userNameToBlock);
        if (user.isEmpty()) {
            messageService.sendMessage(bot, chatId, UserNotFound);
        }
        user.get().setBlocked(true);
        userRepository.save(user.get());
        messageService.sendMessage(bot, chatId, "Пользователь " + userNameToBlock + " успешно добавлен в чёрный список!");
    }


public void removeUserFromBlacklist(TelegramLongPollingBot bot, String message, long chatId) {
    if (!message.startsWith("@")) {
        messageService.sendMessage(bot, chatId, IncorrectData);
        return;
    }
    String userNameToBlock = message.replace("@", "");
    Optional<User> user = userRepository.findByUserName(userNameToBlock);
    if (user.isEmpty()) {
        messageService.sendMessage(bot, chatId, UserNotFound);
        return;
    }
    user.get().setBlocked(false);
    userRepository.save(user.get());
    messageService.sendMessage(bot, chatId, "Пользователь " + userNameToBlock + " успешно удалён из чёрного списка!");
}
}
