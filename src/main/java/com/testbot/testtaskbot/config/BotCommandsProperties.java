package com.testbot.testtaskbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "bot.command")
public class BotCommandsProperties {
    private String begin;
    private String poll;
    private String report;
}
