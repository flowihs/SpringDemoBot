package org.springdemobot.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMessageSenderService {
    private final AdminMessageHandlerService adminMessageHandlerService;

    public void sendMessage(TelegramLongPollingBot bot, Update update, long chatId, String textToSend) {
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
