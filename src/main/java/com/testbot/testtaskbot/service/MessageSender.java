package com.testbot.testtaskbot.service;

import com.testbot.testtaskbot.config.TelegramClientProvider;
import com.testbot.testtaskbot.constant.DefaultMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSender {

    private final TelegramClientProvider telegramClientProvider;

    public void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .build();
        try {
            telegramClientProvider.getTelegramClient().executeAsync(sendMessage);
        } catch (TelegramApiException ex) {
            log.error("Got {} exception while sending message", ex.getMessage());
        }
    }

    public void sendReplyMessage(Long chatId, String text, Integer replyToMessageId) {
        SendMessage errorMessage = SendMessage.builder()
            .chatId(chatId)
            .replyToMessageId(replyToMessageId)
            .text(text)
            .build();
        try {
            telegramClientProvider.getTelegramClient().executeAsync(errorMessage);
        } catch (TelegramApiException ex) {
            log.error("Got {} exception while sending reply message", ex.getMessage());
        }
    }

    public void sendDocument(Long chatId, InputFile inputFile) {
        SendDocument sendDocument = SendDocument.builder()
            .chatId(chatId)
            .document(inputFile)
            .build();
        sendMessage(chatId, DefaultMessages.FILE_READY);
        telegramClientProvider.getTelegramClient().executeAsync(sendDocument);
    }
}
