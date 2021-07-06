package com.smartru.telegram;

import com.smartru.common.model.Calculator;
import com.smartru.performers.calculator.math.ExpressionChecker;
import com.smartru.telegram.commands.IsPrimeNumberCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.DecimalFormat;
import java.text.ParseException;

@Component
public class NonCommandProcess {

    @Autowired
    private Calculator calculator;
    private final DecimalFormat formatter = new DecimalFormat("#0.000");
    private final String DEFAULT_ANSWER = "Привет! Я умею считать простые числа и алгебраические выражения. Напиши команду " +
                                            "/is_prime любое_число (или просто любое число)," +
                                            "и я скажу тебе, простое оно, или нет, или напиши пример, и я решу его. " +
                                            "Введи /help для более подробного описания";

    public String nonCommandExecute(Message message){
        if (ExpressionChecker.isExpression(message.getText())){
            try {
                double result = calculator.perform(message.getText());
                String formattedResult = formatResult(result);
                String answer = message.getText()+" = "+formattedResult;
                return answer;
            } catch (ParseException e) {
                return e.getMessage();
            }
        } else {
            return DEFAULT_ANSWER;
        }
    }

    private String formatResult(double result){
        String formatted = formatter.format(result);
        if (formatted.endsWith(".000")){
            formatted = formatted.substring(0,formatted.length()-4);
        }
        return formatted;
    }
}
