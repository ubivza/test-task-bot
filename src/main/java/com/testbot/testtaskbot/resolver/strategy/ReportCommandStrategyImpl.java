package com.testbot.testtaskbot.resolver.strategy;

import com.testbot.testtaskbot.config.BotCommandsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ReportCommandStrategyImpl implements CommandStrategy {

    private final BotCommandsProperties commandsProperties;

    @Override
    public String getCommand() {
        return commandsProperties.getReport();
    }

    @Override
    public void handle(Update update) {
        System.out.println("Report");
    }
}
