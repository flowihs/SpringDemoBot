package org.springdemobot.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdemobot.model.User;
import org.springdemobot.repository.UserRepository;
import org.springdemobot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMessageHandlerService {
    private final UserRepository userRepository;
    private final static String INCORRECT_DATA = "Вы ввели некорректные данные!";
    private final static String USER_NOT_FOUND = "Вы ввели не существующего пользователя!";
    @Lazy
    @Autowired
    private TelegramBot bot;

    public void processUsernameInput(String messageText, long chatId) {
        if (!messageText.startsWith("@")) {
            bot.sendMessage(chatId, INCORRECT_DATA);
            return;
        }
        String userNameToBlock = messageText.replace("@", "");
        Optional<User> user = userRepository.findByUserName(userNameToBlock);
        if (user.isEmpty()) {
            bot.sendMessage(chatId, USER_NOT_FOUND);
        }
        user.get().setBlocked(true);
        userRepository.save(user.get());
        bot.sendMessage(chatId, "Пользователь " + userNameToBlock + " успешно добавлен в чёрный список!");
    }


    public void removeUserFromBlacklist(String message, long chatId) {
        if (!message.startsWith("@")) {
            bot.sendMessage(chatId, INCORRECT_DATA);
            return;
        }
        String userNameToBlock = message.replace("@", "");
        Optional<User> user = userRepository.findByUserName(userNameToBlock);
        if (user.isEmpty()) {
            bot.sendMessage(chatId, USER_NOT_FOUND);
            return;
        }
        user.get().setBlocked(false);
        userRepository.save(user.get());
        bot.sendMessage(chatId, "Пользователь " + userNameToBlock + " успешно удалён из чёрного списка!");
    }
}
