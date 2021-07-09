package com.smartru.telegram.commands.geometric.keyboards.fabric;

import com.smartru.performers.geometric.shape.Circle;
import com.smartru.performers.geometric.shape.Desired;
import com.smartru.performers.geometric.shape.Rectangle;
import com.smartru.performers.geometric.shape.Shape;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CircleKeyboardFabric extends ShapeKeyboardFabric {

    @Override
    public ReplyKeyboard getShapeKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        String callbackPrefix = "/solve "+ Shape.Type.CIRCLE + " ";

        List<InlineKeyboardButton>propertyButtons = Arrays.stream(Rectangle.Property.values())
                .map(prop->{
                    String rusName = prop.getRusName();
                    rusName = firstCharUpperCase(rusName);
                    return ButtonFabric.createButton(rusName,callbackPrefix+prop.name());
                })
                .collect(Collectors.toList());

        List<List<InlineKeyboardButton>>rows = partition(propertyButtons, 3);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    @Override
    public ReplyKeyboard getShapeParameterKeyboard(Desired desired) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        String callbackPrefix = "/solve "+ Shape.Type.CIRCLE + " "+desired+" ";

        List<InlineKeyboardButton>propertyButtons = Arrays.stream(Rectangle.Property.values())
                .map(prop->{
                    String rusName = prop.getRusName();
                    rusName = firstCharUpperCase(rusName);
                    return ButtonFabric.createButton(rusName,callbackPrefix+prop.name()+" known");
                })
                .collect(Collectors.toList());

        List<List<InlineKeyboardButton>>rows = partition(propertyButtons, 3);

        List<InlineKeyboardButton>lastRow=new ArrayList<>();
        InlineKeyboardButton nothing = ButtonFabric.createButton("Больше ничего",
                callbackPrefix + "end");
        lastRow.add(nothing);
        rows.add(lastRow);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    private List<List<InlineKeyboardButton>> partition(List<InlineKeyboardButton>buttons, int rowSize){
        List<List<InlineKeyboardButton>>rows = new ArrayList<>(buttons.stream()
                .collect(Collectors.groupingBy(button->buttons.indexOf(button)/rowSize))
                .values());
        return rows;
    }

    private String firstCharUpperCase(String word){
        if(word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
