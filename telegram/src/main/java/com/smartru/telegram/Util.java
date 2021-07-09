package com.smartru.telegram;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Util {

    public static SendMessage createMessage(Message message, String text){
        String chatId = String.valueOf(message.getChatId());
        String answer = usernameFromMessage(message)+", "+text;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(answer);
        return sendMessage;
    }

    public static void sendToTelegram(AbsSender sender, SendMessage message){
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendEditMessage(AbsSender sender, EditMessageText message){
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static String usernameFromMessage(Message msg){
        User user = msg.getFrom();
        String username = user.getUserName();
        return "@"+username;
    }
}
