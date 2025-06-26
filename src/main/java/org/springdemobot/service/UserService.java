package org.springdemobot.service;

import lombok.RequiredArgsConstructor;
import org.springdemobot.enums.Role;
import org.springdemobot.model.User;
import org.springdemobot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User registerUserIfNew(Update update, Message message) {
        Long userId = update.getMessage().getFrom().getId();
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            User entity = new User(
                    userId,
                    message.getChat().getFirstName(),
                    message.getChat().getLastName(),
                    message.getChat().getUserName(),
                    false,
                    Role.ROLE_USER,
                    new Timestamp(System.currentTimeMillis())
            );
            return userRepository.save(entity);
        }
        return user.get();
    }
}