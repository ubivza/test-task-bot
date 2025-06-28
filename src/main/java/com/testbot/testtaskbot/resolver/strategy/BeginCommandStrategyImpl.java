package com.testbot.testtaskbot.resolver.strategy;

import com.testbot.testtaskbot.config.BotCommandsProperties;
import com.testbot.testtaskbot.constant.DefaultMessages;
import com.testbot.testtaskbot.service.MessageSender;
import com.testbot.testtaskbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class BeginCommandStrategyImpl implements CommandStrategy {

    private final BotCommandsProperties commandsProperties;
    private final UserSessionService userSessionService;
    private final MessageSender messageSender;

    @Override
    public String getCommand() {
        return commandsProperties.getBegin();
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        userSessionService.revokeUserSession(chatId);

        messageSender.sendMessage(chatId, DefaultMessages.HELLO_MESSAGE
            .formatted(update.getMessage().getFrom().getFirstName() == null ? "незнакомец" : update.getMessage().getFrom().getFirstName()));
    }
}
