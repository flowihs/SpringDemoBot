package org.springdemobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springdemobot.enums.BotMessage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class MessageSenderService {

    private final TelegramLongPollingBot bot;

    public MessageSenderService(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void sendMessage(long chatId, BotMessage message) {
        sendMessage(chatId, message.get());
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage());
        }
    }
}
