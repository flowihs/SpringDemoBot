package org.springdemobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class ModerationService {

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
            log.info("User with ID {} has been tortured in chat {} for {} seconds.", userId, chatId, seconds);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void unmuteUser(TelegramLongPollingBot bot, Long userId, Long chatId) {
        RestrictChatMember unmuteUser = new RestrictChatMember();
        unmuteUser.setChatId(String.valueOf(chatId));
        unmuteUser.setUserId(userId);

        ChatPermissions permissions = new ChatPermissions();
        permissions.setCanSendMessages(true);
        unmuteUser.setPermissions(permissions);

        try {
            bot.execute(unmuteUser);
            log.info("User with ID {} has been tortured in chat {}", userId, chatId);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void banUser(TelegramLongPollingBot bot, Long userId, Long chatId, int seconds) {
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(String.valueOf(chatId));
        banChatMember.setUserId(userId);
        if (seconds > 0) {
            banChatMember.setUntilDate((int) (System.currentTimeMillis() / 1000) + seconds);
        }
        try {
            bot.execute(banChatMember);
            log.info("User with ID {} has been banned in chat {}", userId, chatId);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void unbanUser(TelegramLongPollingBot bot, Long userId, Long chatId) {
        UnbanChatMember unbanChatMember = new UnbanChatMember();
        unbanChatMember.setChatId(String.valueOf(chatId));
        unbanChatMember.setUserId(userId);
        try {
            bot.execute(unbanChatMember);
            log.info("User with ID {} has been unbanned in chat {}", userId, chatId);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
