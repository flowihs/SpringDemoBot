package org.springdemobot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdemobot.model.User;
import org.springdemobot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void registerUserIfNew(Message message) {
        Long chatId = message.getChatId();
        if (userRepository.findById(chatId).isEmpty()) {
            User user = new User(
                    chatId,
                    message.getChat().getFirstName(),
                    message.getChat().getLastName(),
                    message.getChat().getUserName(),
                    new Timestamp(System.currentTimeMillis())
            );
            userRepository.save(user);
        }
    }

    public void muteUser(TelegramLongPollingBot bot, Long userId, Long chatId, int seconds) {
        RestrictChatMember muteUser = new RestrictChatMember();
        muteUser.setChatId(String.valueOf(chatId));
        muteUser.setUserId(userId);

        ChatPermissions permissions = new ChatPermissions();
        permissions.setCanSendMessages(false);
        muteUser.setPermissions(permissions);

        int untilDate = (int) (System.currentTimeMillis() / 1000) + seconds;
        muteUser.setUntilDate(untilDate);

        try {
            bot.execute(muteUser);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}