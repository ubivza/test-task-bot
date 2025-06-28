package com.testbot.testtaskbot.integration;

import com.testbot.testtaskbot.config.BotTokenProperty;
import com.testbot.testtaskbot.constant.DefaultMessages;
import com.testbot.testtaskbot.exception.UnsupportedCommandException;
import com.testbot.testtaskbot.resolver.CommandStrategyResolver;
import com.testbot.testtaskbot.resolver.strategy.CommandStrategy;
import com.testbot.testtaskbot.service.ErrorMessageSender;
import com.testbot.testtaskbot.service.FormProcessService;
import com.testbot.testtaskbot.dto.FormState;
import com.testbot.testtaskbot.service.UserSessionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramUpdatesConsumer implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final CommandStrategyResolver resolver;
    private final BotTokenProperty tokenProperty;
    private final ErrorMessageSender errorMessageSender;
    private final UserSessionService userSessionService;
    private final FormProcessService formProcessService;
    //Использовать лекговесные виртуальные потоки если одновременных пользователей очень много
    private final ExecutorService executorService = Executors.newCachedThreadPool();

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
        executorService.submit(() -> {
            try {
                if (update.getMessage() != null) {
                    Long chatId = update.getMessage().getChatId();
                    String text = update.getMessage().getText();

                    userSessionService.createUserSession(chatId);

                    CommandStrategy strategy = resolver.resolve(text);
                    strategy.handle(update);
                }
            } catch (UnsupportedCommandException e) {
                Long chatId = update.getMessage().getChatId();
                String text = update.getMessage().getText();
                Integer replyToMessageId = update.getMessage().getMessageId();

                if (userSessionService.getUserStates().get(chatId) != FormState.IDLE) {
                    formProcessService.processInput(chatId, replyToMessageId, text);
                } else {
                    errorMessageSender.sendErrorMessage(chatId, replyToMessageId, DefaultMessages.UNSUPPORTED_MESSAGE_ERROR);
                }
            }
        });
    }
}
