package com.smartru.performers.calculator.math;

import java.util.Arrays;
import java.util.function.BinaryOperator;

public enum BiOperator {
    PLUS('+', 1,
            (num1, num2)-> num1+num2),
    MINUS('-', 1,
            (num1, num2)-> num1-num2),
    MULTIPLY('*', 2,
            (num1, num2)-> num1*num2),
    DIVIDE('/', 2,
            (num1, num2)-> num1/num2),
    POW('^', 3,
            Math::pow),
    OPEN_BRACKET('(', 10,
            (num1, num2)->{
                throw new UnsupportedOperationException();
            }),
    CLOSE_BRACKET(')', 10,
            (num1, num2)->{
                throw new UnsupportedOperationException();
            });

    private final char op;
    private final Integer priority;
    private final BinaryOperator<Double> function;

    private BiOperator(char op, Integer priority, BinaryOperator<Double> function){
        this.op = op;
        this.priority = priority;
        this.function = function;
    }

    public double apply(double num1, double num2){
        return function.apply(num1,num2);
    }

    public static boolean isBiOperator(char ch){
        return Arrays.stream(BiOperator.values())
                .map(BiOperator::getOp)
                .anyMatch(op->op==ch);
    }

    public static boolean isBiOperator(String ch){
        return Arrays.stream(BiOperator.values())
                .map(op->String.valueOf(op.getOp()))
                .anyMatch(op->op.equals(ch));
    }

    public static BiOperator getOperator(char ch){
        return Arrays.stream(BiOperator.values())
                .filter(op->op.getOp()==ch)
                .findFirst()
                .get();
    }

    public static BiOperator getOperator(String ch){
        return Arrays.stream(BiOperator.values())
                .filter(op->String.valueOf(op.getOp()).equals(ch))
                .findFirst()
                .get();
    }

    public static Comparator getComparator(){
        return new Comparator();
    }

    public char getOp() {
        return op;
    }

    public Integer getPriority() {
        return priority;
    }

    private static class Comparator implements java.util.Comparator<BiOperator>{

        @Override
        public int compare(BiOperator o1, BiOperator o2) {
            return o1.getPriority().compareTo(o2.getPriority());
        }
    }
}
