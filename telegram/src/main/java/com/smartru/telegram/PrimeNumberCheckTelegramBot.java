package com.smartru.telegram;
import com.smartru.common.entity.TelegramTask;
import com.smartru.telegram.commands.CheckNumberCommand;
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

@Slf4j
@Component
public class PrimeNumberCheckTelegramBot extends TelegramLongPollingCommandBot {

    @Value("${bot.telegram.name}")
    private String BOT_NAME;
    @Value("${bot.telegram.token}")
    private String BOT_TOKEN;
    @Autowired
    private NonCommandProcess nonCommand;
    private CheckNumberCommand checkNumberCommand;

    private final String TASK_PATTERN = "\\A\\d*\\Z";

    public PrimeNumberCheckTelegramBot(CheckNumberCommand checkNumberCommand){
        this.checkNumberCommand =checkNumberCommand;
        register(checkNumberCommand);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        if (isNumber(msg.getText())){
            checkNumber(msg);
            return;
        }

        Long chatId = msg.getChatId();
        String username = usernameFromMessage(msg);
        String answer = nonCommand.nonCommandExecute(chatId, username, msg.getText());
        try {
            setAnswer(chatId,answer);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения пользователю: {}, чат #{}", username, chatId);
        }
    }

    public void resultReturn(TelegramTask task){
        try {
            SendMessage msg = new SendMessage();
            msg.setChatId(String.valueOf(task.getChatId()));

            if (task.getResult().isPrime()){
                msg.setText(String.format("Число %s простое",task.getTask().getNum()));
            } else {
                msg.setText(String.format("Число %s составное",task.getTask().getNum()));
            }
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

    private boolean isNumber(String text){
        return text.matches(TASK_PATTERN);
    }

    private void checkNumber(Message msg){
        checkNumberCommand.processMessage(this,
                msg,
                new String[]{msg.getText()});
    }

    private String usernameFromMessage(Message msg){
        User user = msg.getFrom();
        String username = user.getUserName();
        return username;
    }

    private void setAnswer(Long chatId, String text) throws TelegramApiException {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());
        answer.setText(text);
        execute(answer);
    }
}
