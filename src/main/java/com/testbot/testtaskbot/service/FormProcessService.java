package com.testbot.testtaskbot.service;

import com.testbot.testtaskbot.constant.DefaultMessages;
import com.testbot.testtaskbot.dto.FormState;
import com.testbot.testtaskbot.validator.MessageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormProcessService {

    private final UserSessionService userSessionService;
    private final MessageSender messageSender;
    private final ErrorMessageSender errorMessageSender;
    private final MessageValidator validator;
    private final FormService formService;

    public void startForm(Long chatId) {
        userSessionService.getUserStates().put(chatId, FormState.AWAITING_NAME);
        messageSender.sendMessage(chatId, DefaultMessages.NAME_MESSAGE);
    }

    public void processInput(Long chatId, Integer replyToMessageId, String message) {
        FormState currentState = userSessionService.getUserStates().get(chatId);

        switch (currentState) {
            case AWAITING_NAME -> processName(chatId, message);
            case AWAITING_EMAIL -> processEmail(chatId, replyToMessageId, message);
            case AWAITING_RATING -> processRating(chatId, replyToMessageId, message);
            default -> userSessionService.revokeUserSession(chatId);
        }
    }

    private void processName(Long chatId, String name) {
        userSessionService.getUserForms().get(chatId).setName(name);
        userSessionService.getUserStates().put(chatId, FormState.AWAITING_EMAIL);

        messageSender.sendMessage(chatId, DefaultMessages.EMAIL_MESSAGE);
    }

    private void processEmail(Long chatId, Integer replyToMessageId, String email) {
        boolean isMailValid = validator.validateEmail(email);
        if (!isMailValid) {
            errorMessageSender.sendErrorMessage(chatId, replyToMessageId, DefaultMessages.INVALID_EMAIL_ERROR);
        } else {
            userSessionService.getUserForms().get(chatId).setEmail(email);
            userSessionService.getUserStates().put(chatId, FormState.AWAITING_RATING);

            messageSender.sendMessage(chatId, DefaultMessages.RATE_US_MESSAGE);
        }
    }

    private void processRating(Long chatId, Integer replyToMessageId, String rating) {
        try {
            Integer rate = validator.validateRate(rating);

            userSessionService.getUserForms().get(chatId).setRate(rate);
            userSessionService.getUserForms().get(chatId).setChatId(chatId);
            userSessionService.getUserStates().put(chatId, FormState.IDLE);

            formService.save(userSessionService.getUserForms().get(chatId));

            messageSender.sendMessage(chatId, DefaultMessages.FORM_END_MESSAGE);
        } catch (NumberFormatException ex) {
            errorMessageSender.sendErrorMessage(chatId, replyToMessageId, DefaultMessages.INVALID_NUMBER_FORMAT);
        }
    }
}
