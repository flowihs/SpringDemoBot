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

    public void processUsernameInput(String messageText, long chatId, User user) {
        String userNameToBlock = messageText;
        if (!messageText.startsWith("@") && user.getRole().equals(Role.ROLE_USER)) {
            return;
        }
        Optional<User> user1 = userRepository.findByUserName(userNameToBlock);

        if (user1.isPresent()) {
            user1.get().setBlocked(true);
            userRepository.save(user1.get());
        }

    }
}
