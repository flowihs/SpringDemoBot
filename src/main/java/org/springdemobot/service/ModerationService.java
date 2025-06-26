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

    private final MessageSenderService messageSenderService;

    public ModerationService(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    public RestrictChatMember muteUser(TelegramLongPollingBot bot, Long userId, Long chatId, int seconds) {
        RestrictChatMember muteUser = new RestrictChatMember();
        muteUser.setChatId(String.valueOf(chatId));
        muteUser.setUserId(userId);

        ChatPermissions permissions = new ChatPermissions();
        permissions.setCanSendMessages(false);
        permissions.setCanSendMediaMessages(false);
        permissions.setCanSendPolls(false);
        permissions.setCanSendOtherMessages(false);
        permissions.setCanAddWebPagePreviews(false);
        muteUser.setPermissions(permissions);

        if (seconds > 0) {
            int untilDate = (int) (System.currentTimeMillis() / 1000) + seconds;
            muteUser.setUntilDate(untilDate);
        }
        return muteUser;
    }

    public RestrictChatMember unmuteUser(TelegramLongPollingBot bot, Long userId, Long chatId) {
        RestrictChatMember unmuteUser = new RestrictChatMember();
        unmuteUser.setChatId(String.valueOf(chatId));
        unmuteUser.setUserId(userId);

        ChatPermissions permissions = new ChatPermissions();
        permissions.setCanSendMessages(true);
        unmuteUser.setPermissions(permissions);
        return unmuteUser;
    }

    public BanChatMember banUser(TelegramLongPollingBot bot, Long userId, Long chatId, int seconds) {
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(String.valueOf(chatId));
        banChatMember.setUserId(userId);
        if (seconds > 0) {
            banChatMember.setUntilDate((int) (System.currentTimeMillis() / 1000) + seconds);
        }
        return banChatMember;
    }

    public UnbanChatMember unbanUser(TelegramLongPollingBot bot, Long userId, Long chatId) {
        UnbanChatMember unbanChatMember = new UnbanChatMember();
        unbanChatMember.setChatId(String.valueOf(chatId));
        unbanChatMember.setUserId(userId);
        return unbanChatMember;
    }


}
