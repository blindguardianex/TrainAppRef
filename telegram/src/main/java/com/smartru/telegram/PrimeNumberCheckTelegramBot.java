package com.smartru.telegram;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
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
    private final ObjectMapper mapper = new ObjectMapper();

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
        } else {
            sendDefaultMessage(msg);
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

    private boolean isNumber(String text){
        return text.matches(TASK_PATTERN);
    }

    private void checkNumber(Message msg){
        checkNumberCommand.processMessage(this,
                msg,
                new String[]{msg.getText()});
    }

    private void sendDefaultMessage(Message msg){
        Long chatId = msg.getChatId();
        String username = usernameFromMessage(msg);
        String answer = nonCommand.nonCommandExecute(chatId, username, msg.getText());
        try {
            setAnswer(chatId,answer);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения пользователю: {}, чат #{}", username, chatId);
        }
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

    private void setAnswer(Long chatId, String text) throws TelegramApiException {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());
        answer.setText(text);
        execute(answer);
    }

    private String getChatIdFromTask(Task task){
        return task.getProperties().get("chatId").asText();
    }
}
