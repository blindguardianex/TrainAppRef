package com.smartru.telegram.commands;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TelegramTask;
import com.smartru.common.entity.User;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.common.service.jpa.UserService;
import com.smartru.common.service.rabbitmq.TelegramTaskRabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Component
public class CheckNumberCommand implements IBotCommand {

    private final String IDENTIFIER = "check";
    private final String DESCRIPTION = "Проверить число";
    private final String NUMERIC_PATTERN = "\\A\\d*\\Z";

    private User TELEGRAM_USER;
    private final TaskService taskService;
    private final TelegramTaskRabbitService taskRabbitService;

    private ThreadLocal<AbsSender>localSender = new ThreadLocal<>();
    private ThreadLocal<Message>localMessage = new ThreadLocal<>();

    public CheckNumberCommand(UserService userService, TaskService taskService, TelegramTaskRabbitService taskRabbitService) {
        this.taskService = taskService;
        this.taskRabbitService = taskRabbitService;
        initializeTelegramUser(userService);
    }

    @Override
    public String getCommandIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        localSender.set(absSender);
        localMessage.set(message);
        try {
            final String num = strings[0];
            if (numIsCorrect(num)){
                sendDefaultPreAnswer(num);
                sendTypingEvent();

                TelegramTask task = createTelegramTask(num, message.getChatId());
                sendTelegramTaskForExecution(task);
            } else {
                sendErrorMessage(num);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean numIsCorrect(String num){
        return messageIsNumeric(num) && !numIsTooLong(num);
    }

    private void sendDefaultPreAnswer(String num) throws TelegramApiException {
        if (num.length()>13) {
            SendMessage msg = new SendMessage(localMessage.get().getChatId().toString(),
                    "Я отправлю тебе ответ как только полностью проверю число: #" + num);
            localSender.get().execute(msg);
        }
    }

    private void sendErrorMessage(String num) throws TelegramApiException {
        if (!messageIsNumeric(num)){
            SendMessage msg = new SendMessage(localMessage.get().getChatId().toString(),
                    "Извини, но я работаю только с числами.");
            localSender.get().execute(msg);
        } else if (numIsTooLong(num)) {
            SendMessage msg = new SendMessage(localMessage.get().getChatId().toString(),
                    "Извини, но я не могу проверить такое число! Максимальная длина числа: 23 разряда");
            localSender.get().execute(msg);
        } else {
            SendMessage msg = new SendMessage(localMessage.get().getChatId().toString(),
                    "По какой-то причине твое сообщение принято некорректным! Возможно, это гендерное неравенство...");
            localSender.get().execute(msg);
        }
    }

    private TelegramTask createTelegramTask(String num, long chatId){
        Task task = new Task(num);
        task.setUser(TELEGRAM_USER);
        taskService.add(task);
        return new TelegramTask(task,chatId);
    }

    private void sendTypingEvent() throws TelegramApiException {
        SendChatAction action = new SendChatAction();
        action.setChatId(localMessage.get().getChatId().toString());
        action.setAction(ActionType.TYPING);
        localSender.get().execute(action);
    }

    private boolean messageIsNumeric(String message){
        return message.matches(NUMERIC_PATTERN);
    }

    private boolean numIsTooLong(String num){
        return num.length()>23;
    }

    private void sendTelegramTaskForExecution(TelegramTask task){
        taskRabbitService.sendTelegramTask(task);
    }

    private void initializeTelegramUser(UserService service){
        this.TELEGRAM_USER = service.getByUsername("telegram_user").orElseThrow(EntityNotFoundException::new);
    }
}
