package org.springdemobot.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminMessageSenderService {
    @Lazy
    @Autowired
    private TelegramLongPollingBot bot;

    public void sendMessageAndKeyboard(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        final var keyboardMarkup = keyboardMarkup();
        message.setReplyMarkup(keyboardMarkup);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage());
        }
    }

    public ReplyKeyboardMarkup ownerKeyboard() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Назначить админа");
        ReplyKeyboardMarkup keyboard = keyboardMarkup();
        keyboard.setKeyboard(List.of(keyboardRow));
        return keyboard;
    }

    public ReplyKeyboardMarkup keyboardMarkup() {
        final var replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add("Добавить в чёрный список");
        row.add("Удалить из чёрного списка");

        replyKeyboardMarkup.setKeyboard(List.of(row));
        return replyKeyboardMarkup;
    }
}
