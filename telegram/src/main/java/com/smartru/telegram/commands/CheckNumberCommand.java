package com.smartru.telegram.commands;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TelegramTask;
import com.smartru.common.entity.User;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.common.service.jpa.UserService;
import com.smartru.common.service.rabbitmq.TaskRabbitService;
import com.smartru.telegram.PrimeNumberCheckTelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
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
    private final String TASK_PATTERN = "\\A\\d*\\Z";

    private User TELEGRAM_USER;
    private final TaskService taskService;
    private final TaskRabbitService taskRabbitService;

    public CheckNumberCommand(UserService userService, TaskService taskService, TaskRabbitService taskRabbitService) {
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
        try {
            String num = strings[0];
            SendMessage msg = new SendMessage();
            msg.setChatId(String.valueOf(message.getChatId()));

            if (numIsCorrect(num)){
                msg.setText("Я отправлю тебе ответ как только полностью проверю число: #"+strings[0]);
                absSender.execute(msg);
                TelegramTask task = saveTelegramTask(num, message.getChatId());
                sendTelegramTask(task);
            } else {
                msg.setText("Извини, но я не могу проверить такое число! " +
                        "Максимальная длина числа: 23 разряда");
                absSender.execute(msg);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean numIsCorrect(String num){
        if (num.matches(TASK_PATTERN) && num.length()<24){
            return true;
        }
        else {
            return false;
        }
    }

    private TelegramTask saveTelegramTask(String num, long chatId){
        Task task = new Task(num);
        task.setUser(TELEGRAM_USER);
        taskService.add(task);
        return new TelegramTask(task,chatId);
    }

    private void sendTelegramTask(TelegramTask task){
        taskRabbitService.sendTelegramTask(task);
    }

    private void initializeTelegramUser(UserService service){
        this.TELEGRAM_USER = service.getByUsername("telegram_user").orElseThrow(EntityNotFoundException::new);
    }
}
