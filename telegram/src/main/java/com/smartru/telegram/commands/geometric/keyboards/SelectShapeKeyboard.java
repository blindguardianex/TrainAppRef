package com.smartru.telegram.commands.geometric.keyboards;

import com.smartru.performers.geometric.shape.Rectangle;
import com.smartru.performers.geometric.shape.Shape;
import com.smartru.telegram.commands.geometric.keyboards.fabric.ButtonFabric;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectShapeKeyboard {

    public static InlineKeyboardMarkup get(){
        final String callbackPrefix = "/solve ";

        List<InlineKeyboardButton>shapeButtons = Arrays.stream(Shape.Type.values())
                .map(type->{
                    String rusName = type.getRusName();
                    return ButtonFabric.createButton(rusName,callbackPrefix+type.name());
                })
                .collect(Collectors.toList());

        List<List<InlineKeyboardButton>>rows= partition(shapeButtons, 3);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rows);

        return keyboard;
    }

    private static List<List<InlineKeyboardButton>> partition(List<InlineKeyboardButton>shapes, int rowSize){
        List<List<InlineKeyboardButton>>rows = new ArrayList<>(shapes.stream()
                .collect(Collectors.groupingBy(button->shapes.indexOf(button)/rowSize))
                .values());
        return rows;
    }
}
