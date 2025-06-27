package com.testbot.testtaskbot.resolver;

import com.testbot.testtaskbot.exception.UnsupportedCommandException;
import com.testbot.testtaskbot.resolver.strategy.CommandStrategy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CommandStrategyResolver {

    private final Map<String, CommandStrategy> strategyMap;

    public CommandStrategyResolver(List<CommandStrategy> strategyList) {
        strategyMap = strategyList.stream()
            .collect(Collectors.toMap(
                CommandStrategy::getCommand,
                Function.identity()
            ));
    }

    public CommandStrategy resolve(String command) {
        return Optional.ofNullable(strategyMap.get(command))
            .orElseThrow(() -> new UnsupportedCommandException("Command not supported"));
    }
}
