package com.testbot.testtaskbot.dto;

import lombok.Data;

@Data
public class UserForm {

    private Long chatId;
    private String name;
    private String email;
    private int rate;
}
