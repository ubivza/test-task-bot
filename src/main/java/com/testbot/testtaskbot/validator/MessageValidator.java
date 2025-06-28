package com.testbot.testtaskbot.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component
public class MessageValidator {

    public Integer validateRate(String rating) {
        Integer rate = Integer.parseInt(rating);
        if (rate > 10 || rate < 1) {
            throw new NumberFormatException();
        }
        return rate;
    }

    public boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EmailValidator.getInstance().isValid(email);
    }
}
