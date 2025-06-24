package org.springdemobot.service;

import lombok.RequiredArgsConstructor;
import org.springdemobot.model.User;
import org.springdemobot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;

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
}