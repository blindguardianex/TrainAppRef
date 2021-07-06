package com.smartru.performers.calculator.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpressionIterator implements Iterator<Character> {

    private final char[] expression;
    private int cursor;

    public ExpressionIterator(String expression) {
        this.expression = expression.toCharArray();
    }

    @Override
    public boolean hasNext() {
        return cursor!=expression.length;
    }

    @Override
    public Character next() {
        return expression[cursor++];
    }
}
