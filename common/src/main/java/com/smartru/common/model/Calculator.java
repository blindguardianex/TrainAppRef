package com.smartru.common.model;

import java.text.ParseException;

public interface Calculator {

    double solve(String expression) throws ParseException;
}
