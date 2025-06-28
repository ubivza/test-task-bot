package com.testbot.testtaskbot.resolver.strategy;

import com.testbot.testtaskbot.config.BotCommandsProperties;
import com.testbot.testtaskbot.facade.WordReportFacade;
import com.testbot.testtaskbot.service.FormService;
import com.testbot.testtaskbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ReportCommandStrategyImpl implements CommandStrategy {

    private final BotCommandsProperties commandsProperties;
    private final UserSessionService userSessionService;
    private final WordReportFacade wordReportFacade;
    private final FormService formService;

    @Override
    public String getCommand() {
        return commandsProperties.getReport();
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        userSessionService.revokeUserSession(chatId);

        wordReportFacade.generateReport(chatId, formService.getAll());
    }
}
