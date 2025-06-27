package com.testbot.testtaskbot.integration;

import com.testbot.testtaskbot.config.BotTokenProperty;
import com.testbot.testtaskbot.exception.UnsupportedCommandException;
import com.testbot.testtaskbot.resolver.CommandStrategyResolver;
import com.testbot.testtaskbot.resolver.strategy.CommandStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class TelegramUpdatesConsumer implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final CommandStrategyResolver resolver;
    private final BotTokenProperty tokenProperty;
    private final static String ERROR_MESSAGE = "Такая команда или сообщение пока не поддерживается \uD83D\uDE31\uD83D\uDE31\uD83D\uDE31";

    public TelegramUpdatesConsumer(CommandStrategyResolver resolver, BotTokenProperty tokenProperty) {
        this.tokenProperty = tokenProperty;
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
        this.resolver = resolver;
    }

    @Override
    public String getBotToken() {
        return tokenProperty.getValue();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        try {
            if (update.getMessage() != null) {
                CommandStrategy strategy = resolver.resolve(update.getMessage().getText());
                strategy.handle(update);
            }
        } catch (UnsupportedCommandException e) {
            sendErrorMessage(update);
        }
    }

    private void sendErrorMessage(Update update) {
        SendMessage errorMessage = SendMessage.builder()
            .chatId(update.getMessage().getChatId())
            .replyToMessageId(update.getMessage().getMessageId())
            .text(ERROR_MESSAGE)
            .build();
        try {
            telegramClient.executeAsync(errorMessage);
        } catch (TelegramApiException ex) {
            log.error("Got {} exception while sending errorMessage", ex.getMessage());
        }
    }
}
