package com.smartru.performers.calculator.math;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public enum UOperator {

    SQRT("SQRT", Math::sqrt),
    COS("COS",degree->{
        double radians = Math.toRadians(degree);
        return Math.cos(radians);
    }),
    SIN("SIN",degree->{
        double radians = Math.toRadians(degree);
        return Math.sin(radians);
    }),
    TAN("TAN",degree->{
        double radians = Math.toRadians(degree);
        return Math.tan(radians);
    });

    private final String name;
    private final UnaryOperator<Double> function;

    UOperator(String name, UnaryOperator<Double> function) {
        this.name = name;
        this.function = function;
    }

    public static boolean isUOperator(String funcName){
        return Arrays.stream(UOperator.values())
                .anyMatch(op->op.name().equalsIgnoreCase(funcName));
    }

    public static UOperator getOperator(String name){
        return Arrays.stream(UOperator.values())
                .filter(op->op.name().equalsIgnoreCase(name))
                .findFirst()
                .get();
    }

    public double apply(double num){
        return function.apply(num);
    }
}
