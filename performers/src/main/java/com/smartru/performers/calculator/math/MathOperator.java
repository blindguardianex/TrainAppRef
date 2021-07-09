package com.smartru.performers.calculator.math;

import java.util.Arrays;
import java.util.function.BinaryOperator;

public enum MathOperator {
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

    private MathOperator(char op, Integer priority, BinaryOperator<Double> function){
        this.op = op;
        this.priority = priority;
        this.function = function;
    }

    public double apply(double num1, double num2){
        return function.apply(num1,num2);
    }

    public static boolean isMathOperator(char ch){
        return Arrays.stream(MathOperator.values())
                .map(MathOperator::getOp)
                .anyMatch(op->op==ch);
    }

    public static boolean isMathOperator(String ch){
        return Arrays.stream(MathOperator.values())
                .map(op->String.valueOf(op.getOp()))
                .anyMatch(op->op.equals(ch));
    }

    public static MathOperator getMathOperatorBy(char ch){
        return Arrays.stream(MathOperator.values())
                .filter(op->op.getOp()==ch)
                .findFirst()
                .get();
    }

    public static MathOperator getMathOperatorBy(String ch){
        return Arrays.stream(MathOperator.values())
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

    private static class Comparator implements java.util.Comparator<MathOperator>{

        @Override
        public int compare(MathOperator o1, MathOperator o2) {
            return o1.getPriority().compareTo(o2.getPriority());
        }
    }
}
