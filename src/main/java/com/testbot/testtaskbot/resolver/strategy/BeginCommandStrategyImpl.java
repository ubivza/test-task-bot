package com.testbot.testtaskbot.resolver.strategy;

import com.testbot.testtaskbot.config.BotCommandsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class BeginCommandStrategyImpl implements CommandStrategy {

    private final BotCommandsProperties commandsProperties;

    @Override
    public String getCommand() {
        return commandsProperties.getBegin();
    }

    @Override
    public void handle(Update update) {
        System.out.println("Begin");
    }
}
