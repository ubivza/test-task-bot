package com.testbot.testtaskbot.resolver.strategy;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandStrategy {

    String getCommand();
    void handle(Update update);
}
