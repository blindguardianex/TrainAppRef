package com.smartru.telegram.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smartru.common.entity.Task;
import com.smartru.common.entity.User;
import com.smartru.common.service.jpa.TaskService;
import com.smartru.common.service.jpa.UserService;
import com.smartru.common.service.messagebroker.TaskBrokerService;
import com.smartru.telegram.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class DecomposeNumberCommand implements IBotCommand {

    private final String IDENTIFIER = "decompose";
    private final String DESCRIPTION = "Разложить число на множители";
    private final String NUMERIC_PATTERN = "\\A\\d*\\Z";

    private User TELEGRAM_USER;
    private final TaskService taskService;
    private final TaskBrokerService taskBrokerService;
    private final ObjectMapper mapper;

    private ThreadLocal<AbsSender>localSender = new ThreadLocal<>();
    private ThreadLocal<Message>localMessage = new ThreadLocal<>();

    @Autowired
    public DecomposeNumberCommand(UserService userService,
                                  TaskService taskService,
                                  @Qualifier("telegramTaskRabbitService") TaskBrokerService taskBrokerService,
                                  ObjectMapper mapper) {
        this.taskService = taskService;
        this.taskBrokerService = taskBrokerService;
        this.mapper = mapper;
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
        final String num = strings[0];
        if (numIsCorrect(num)){
            sendDefaultPreAnswer(num);
            sendTypingEvent();

            Task task = createTask(num, message);
            sendTelegramTaskForExecution(task);
        } else {
            sendErrorMessage(num);
        }
    }

    private boolean numIsCorrect(String num){
        return isNumber(num) && !numIsTooLarge(num);
    }

    private void sendDefaultPreAnswer(String num){
        if (num.length()>13) {
            SendMessage msg = Util.createMessage(localMessage.get(),
                    String.format("я отправлю тебе ответ как только полностью проверю число: #" + num));
            sendToTelegram(msg);
        }
    }

    private void sendErrorMessage(String num){
        SendMessage msg;
        if (!isNumber(num)){
            msg = Util.createMessage(localMessage.get(), "извини, но я работаю только с числами.");
        } else if (numIsTooLarge(num)) {
            msg = Util.createMessage(localMessage.get(), "извини, но я не могу проверить такое число! Максимальная длина числа: 23 разряда");
        } else {
            msg = Util.createMessage(localMessage.get(), "по какой-то причине твое сообщение принято некорректным! Возможно, это гендерное неравенство...");
        }
        sendToTelegram(msg);
    }

    private Task createTask(String num, Message message){
        Task task = new Task(num);
        task.setUser(TELEGRAM_USER);
        task.setProperties(createTaskProperties(message));
        taskService.add(task);
        return task;
    }

    /**
     * TODO Пока что не работает (???)
     */
    private void sendTypingEvent(){
        SendChatAction action = new SendChatAction();
        action.setChatId(localMessage.get().getChatId().toString());
        action.setAction(ActionType.TYPING);
        try {
            localSender.get().execute(action);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean isNumber(String message){
        return message.matches(NUMERIC_PATTERN);
    }

    private boolean numIsTooLarge(String num){
        return num.length()>23;
    }

    private void sendTelegramTaskForExecution(Task task){
        taskBrokerService.sendTask(task);
    }

    private void initializeTelegramUser(UserService service){
        this.TELEGRAM_USER = service.getByUsername("telegram_user")
                .orElseThrow(EntityNotFoundException::new);
    }

    private ObjectNode createTaskProperties(Message message){
        ObjectNode properties = mapper.createObjectNode();
        properties.put("type", Task.Type.TELEGRAM.toString());
        properties.put("chatId", message.getChatId());
        properties.put("username", usernameFromMessage(message));
        return properties;
    }

    private void sendToTelegram(SendMessage message){
        try {
            localSender.get().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String usernameFromMessage(Message msg){
        org.telegram.telegrambots.meta.api.objects.User user = msg.getFrom();
        String username = user.getUserName();
        return "@"+username;
    }
}
