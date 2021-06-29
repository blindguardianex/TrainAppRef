package com.smartru.telegram;

import org.springframework.stereotype.Component;

@Component
public class NonCommandProcess {

    private final String DEFAULT_ANSWER = "Привет! Я умею считать простые числа. Напиши команду " +
                                            "/check любое_число (или просто любое число)," +
                                            "и я скажу тебе, простое оно, или нет.";

    public String nonCommandExecute(long chatId, String username, String text){
        return DEFAULT_ANSWER;
    }
}
