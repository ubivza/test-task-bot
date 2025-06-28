package com.testbot.testtaskbot.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DefaultMessages {

    //Можно вынести в переменные чтобы менять текст упрощенно, либо через админку
    public final static String UNSUPPORTED_MESSAGE_ERROR = "Такая команда или сообщение пока не поддерживается \uD83D\uDE31\uD83D\uDE31\uD83D\uDE31";
    public final static String INVALID_EMAIL_ERROR = "Введенный имейл не валиден, пожалуйста, проверьте его и отправьте еще раз \uD83E\uDEE3\uD83E\uDD7A";
    public final static String INVALID_NUMBER_FORMAT = "Введите число от 1 до 10 \uD83E\uDD15";
    public final static String HELLO_MESSAGE = "Привет %s! Доступные команды для бота находятся слева от окна набора сообщения \uD83D\uDC47\uD83D\uDC48";
    public final static String NAME_MESSAGE = "Как вас представить? \uD83E\uDD78";
    public final static String EMAIL_MESSAGE = "Отправьте ваш имейл (никакого спама \uD83D\uDE07): ";
    public final static String RATE_US_MESSAGE = "Оцените работу нашего сервиса от 1 до 10 \uD83E\uDEF6: ";
    public final static String FORM_END_MESSAGE = "Спасибо что оценили нас! \uD83E\uDEF5\uD83D\uDC4D";
    public final static String FILE_READY = "Ваш отчет готов! \uD83C\uDF89\uD83C\uDF89";
}
