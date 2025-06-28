package com.testbot.testtaskbot.config;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class TelegramClientProvider {

    private final TelegramClient telegramClient;

    public TelegramClientProvider(BotTokenProperty tokenProperty) {
        this.telegramClient = new OkHttpTelegramClient(tokenProperty.getValue());
    }

    public TelegramClient getTelegramClient() {
        return telegramClient;
    }
}
