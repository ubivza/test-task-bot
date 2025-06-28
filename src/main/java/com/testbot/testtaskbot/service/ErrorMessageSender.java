package com.testbot.testtaskbot.service;

import com.testbot.testtaskbot.config.TelegramClientProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorMessageSender {

    private final TelegramClientProvider telegramClientProvider;

    public void sendErrorMessage(Long chatId, Integer replyToMessageId, String text) {
        SendMessage errorMessage = SendMessage.builder()
            .chatId(chatId)
            .replyToMessageId(replyToMessageId)
            .text(text)
            .build();
        try {
            telegramClientProvider.getTelegramClient().executeAsync(errorMessage);
        } catch (TelegramApiException ex) {
            log.error("Got {} exception while sending errorMessage", ex.getMessage());
        }
    }
}
