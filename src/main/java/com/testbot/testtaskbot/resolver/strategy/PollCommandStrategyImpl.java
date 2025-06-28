package com.testbot.testtaskbot.resolver.strategy;

import com.testbot.testtaskbot.config.BotCommandsProperties;
import com.testbot.testtaskbot.service.FormProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class PollCommandStrategyImpl implements CommandStrategy {

    private final BotCommandsProperties commandsProperties;
    private final FormProcessService formProcessService;

    @Override
    public String getCommand() {
        return commandsProperties.getPoll();
    }

    @Override
    public void handle(Update update) {
        formProcessService.startForm(update.getMessage().getChatId());
    }
}
