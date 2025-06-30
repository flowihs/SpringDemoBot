package org.springdemobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springdemobot.enums.Role;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Service
public class BotCommandService {

    public void initializeCommands(AbsSender bot) {
        List<BotCommand> commands = List.of(
                new BotCommand("/start", "get a welcome message"),
                new BotCommand("/help", "info how to use this bot"),
                new BotCommand("/settings", "set your preferences")
        );
       try {
            bot.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot commands: {}", e.getMessage());
        }
    }
}
