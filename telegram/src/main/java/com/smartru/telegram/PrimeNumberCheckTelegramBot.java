package com.smartru.telegram;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartru.common.entity.Task;
import com.smartru.common.model.Calculator;
import com.smartru.telegram.commands.IsPrimeNumberCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

/**
 * TODO: Добавить функцию ответа на изменененные сообщения
 */
@Slf4j
@Component
public class PrimeNumberCheckTelegramBot extends TelegramLongPollingCommandBot {

    @Value("${bot.telegram.name}")
    private String BOT_NAME;
    @Value("${bot.telegram.token}")
    private String BOT_TOKEN;
    @Autowired
    private NonCommandProcess nonCommand;
    @Autowired
    private Calculator calculator;
    private IsPrimeNumberCommand isPrimeNumberCommand;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Date START_DATE;
    private final String NUMBER_PATTERN = "\\A\\d*\\Z";


    public PrimeNumberCheckTelegramBot(IsPrimeNumberCommand isPrimeNumberCommand){
        this.isPrimeNumberCommand = isPrimeNumberCommand;
        START_DATE = new Date();
        register(isPrimeNumberCommand);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        if (outOfDate(msg)) {
            return;
        }
        if (isNumber(msg.getText())){
            checkNumber(msg);
        } else {
            String answer = nonCommand.nonCommandExecute(msg);
            setAnswer(msg.getChatId(), answer);
        }
    }

    public void resultReturn(Task task){
        try {
            SendMessage msg = new SendMessage();
            msg.setChatId(getChatIdFromTask(task));
            msg.setText(createResponseByResult(task));
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    /**
     * TODO: РЕАЛИЗОВАТЬ
     * @param message
     * @return
     */
    private boolean outOfDate(Message message){
        return false;
    }

    private boolean isNumber(String text){
        return text.matches(NUMBER_PATTERN);
    }

    private void checkNumber(Message msg){
        isPrimeNumberCommand.processMessage(this,
                msg,
                new String[]{msg.getText()});
    }

    private String createResponseByResult(Task task){
        if (task.getResult().isPrime()){
            return String.format("Число %s простое",task.getNum());
        } else {
            return String.format("Число %s составное",task.getNum());
        }
    }

    private String usernameFromMessage(Message msg){
        User user = msg.getFrom();
        String username = user.getUserName();
        return username;
    }

    private void setAnswer(Long chatId, String text){
        try {
            SendMessage answer = new SendMessage();
            answer.setChatId(chatId.toString());
            answer.setText(text);
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getChatIdFromTask(Task task){
        return task.getProperties().get("chatId").asText();
    }
}
