package org.springdemobot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdemobot.enums.BotMessage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageHandlerService {
    private final UserService userService;
    private final MessageSenderService messageSenderService;
    private final ModerationService moderationService;
    private final ProfanityCheckerService profanityCheckerService;

    public void handleUpdate(Update update, TelegramLongPollingBot bot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (profanityCheckerService.isProfanity(messageText)) {
                if (message.isGroupMessage() || message.isSuperGroupMessage()) {
                    try {
                        bot.execute(new DeleteMessage(String.valueOf(chatId), message.getMessageId()));
                        bot.execute(moderationService.muteUser(bot, message.getFrom().getId(), message.getChatId(), 640));
                        messageSenderService.sendMessage(bot, message.getFrom().getId(), BotMessage.MUTED_FOR_PROFANITY.get());
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                }
                return;
            }


            switch (messageText) {
                case "/start":
                    userService.registerUserIfNew(update.getMessage());
                    messageSenderService.sendMessage(bot, chatId, BotMessage.WELCOME.get());
                    break;
                case "/help":
                    messageSenderService.sendMessage(bot, chatId, BotMessage.HELP_MESSAGE.get());
                    break;

                default:
                    messageSenderService.sendMessage(bot, chatId, BotMessage.UNKNOWN_COMMAND.get());
            }
        }
    }
}