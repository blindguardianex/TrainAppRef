package com.smartru.telegram.commands;

import com.smartru.telegram.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class HelpCommand implements IBotCommand {
    private final String IDENTIFIER = "help";
    private final String DESCRIPTION = "Справка";
    private final String HELP_ANSWER = "ввод команды /help - эта справка.\n" +
            "Ввод числа с командой /F - вывод факториала числа.\n" +
            "Ввод числа без команды (или с командой /decompose) - разложение на множители. Максимальный размер числа для проверки: 23 разряда.\n" +
            "Ввод арифметического выражения без команды - решение выражения. \nПоддерживаются следующие операторы и функции:\n" +
            "+ - сложение\n"+
            "- - вычитание\n"+
            "* - умножение\n"+
            "/ - деление\n"+
            "^ - возведение в степень\n"+
            "() - выражения со скобками\n"+
            "Функции: \n" +
            "SQRT - квадратный корень\n"+
            "SIN - синус\n"+
            "ASIN - арксинус\n"+
            "COS - косинус\n"+
            "ACOS - арккосинус\n"+
            "TAN - тангенс\n"+
            "ATAN - арктангенс\n"+
            "CTN - катангенс\n"+
            "ACTN - арккатангенс\n"+
            "ABS - модуль числа\n"+
            "LG - логарифм по основанию 10\n"+
            "LN - натуральный логарифм\n"+
            "DEGREES - перевод радиан в градусы\n"+
            "RADIANS - перевод градусов в радианы\n" +
            "Все тригонометрические функции принимают градусы.\n" +
            "Все функции имеют запись вида : ФУНКЦИЯ(ВЫРАЖЕНИЕ).\n" +
            "Регистр символов не важен.\n" +
            "При записи дробных чисел пожно использовать как \".\", так и \",\".";

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        SendMessage msg = Util.createMessage(message, HELP_ANSWER);
        try {
            absSender.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCommandIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
