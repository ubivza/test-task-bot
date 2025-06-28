package com.testbot.testtaskbot.service;

import com.testbot.testtaskbot.dto.FormState;
import com.testbot.testtaskbot.dto.UserForm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UserSessionService {

    //map -> Redis если нужно масштабирование
    private final Map<Long, FormState> userStates = new ConcurrentHashMap<>();
    private final Map<Long, UserForm> userForms = new ConcurrentHashMap<>();
    private final Map<Long, Instant> sessionTime = new ConcurrentHashMap<>();

    public void createUserSession(Long chatId) {
        userStates.computeIfAbsent(chatId, k -> FormState.IDLE);
        userForms.computeIfAbsent(chatId, k -> new UserForm());
        sessionTime.put(chatId, Instant.now());
    }

    public void revokeUserSession(Long chatId) {
        userStates.put(chatId, FormState.IDLE);
        userForms.put(chatId, new UserForm());
    }

    //Rate и время жизни сессии можно вынести в переменные окружения
    @Async
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void deleteExpiredUserSessions() {
        Instant now = Instant.now();
        Instant expirationTime = now.minus(10, ChronoUnit.MINUTES);

        List<Long> expiredSessions = sessionTime.entrySet().stream()
            .filter(entry -> entry.getValue().isBefore(expirationTime))
            .map(Map.Entry::getKey)
            .toList();

        if (!expiredSessions.isEmpty()) {
            expiredSessions.forEach(chatId -> {
                userStates.remove(chatId);
                userForms.remove(chatId);
                sessionTime.remove(chatId);
            });
        }
    }
}
