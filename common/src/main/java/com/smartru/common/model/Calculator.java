package com.smartru.common.model;

import java.text.ParseException;

public interface Calculator {

    double perform(String expression) throws ParseException;
}
