package com.smartru.telegram;

import org.springframework.stereotype.Component;

@Component
public class NonCommandProcess {

    public String nonCommandExecute(long chatId, String username, String text){
        String answer = "Привет! Я умею считать простые числа. Напиши команду /check любое_число," +
                "и я скажу тебе, простое оно, или нет.";
        return answer;
    }
}
