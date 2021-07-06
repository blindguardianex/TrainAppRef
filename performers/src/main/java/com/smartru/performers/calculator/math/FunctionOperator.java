package com.smartru.performers.calculator.math;

import java.util.Arrays;
import java.util.function.UnaryOperator;

/**
 * !!!ВНИМАНИЕ!!!
 * !!!При добавлении новой функции ее необходимо добавить в проверяющее регулярное
 * выражение в классе ExpressionChecker!!!
 * @see ExpressionChecker
 */
public enum FunctionOperator {

    SQRT("SQRT", Math::sqrt),
    LN("LN", Math::log),
    LG("LG", Math::log10),
    ABS("ABS",Math::abs),
    DEGREES("DEGREES",Math::toDegrees),
    RADIANS("RADIANS",Math::toRadians),
    SIN("SIN",degree->{
        double radians = Math.toRadians(degree);
        return Math.sin(radians);
    }),
    ASIN("ASIN", degree->{
        double radians = Math.toRadians(degree);
        return Math.asin(radians);
    }),
    COS("COS",degree->{
        double radians = Math.toRadians(degree);
        return Math.cos(radians);
    }),
    ACOS("ACOS", degree->{
        double radians = Math.toRadians(degree);
        return Math.acos(radians);
    }),
    TAN("TAN",degree->{
        double radians = Math.toRadians(degree);
        return Math.tan(radians);
    }),
    ATAN("ATAN", degree->{
        double radians = Math.toRadians(degree);
        return Math.atan(radians);
    }),
    CTN("CTN",degree->{
        double radians = Math.toRadians(degree);
        return 1/Math.tan(radians);
    }),
    ACTN("ACTN", degree->{
        double radians = Math.toRadians(degree);
        return Math.atan(radians)+2*Math.atan(1);
    });

    private final String name;
    private final UnaryOperator<Double> function;

    FunctionOperator(String name, UnaryOperator<Double> function) {
        this.name = name;
        this.function = function;
    }

    public static boolean isUOperator(String funcName){
        return Arrays.stream(FunctionOperator.values())
                .anyMatch(op->op.name().equalsIgnoreCase(funcName));
    }

    public static FunctionOperator getOperator(String name){
        return Arrays.stream(FunctionOperator.values())
                .filter(op->op.name().equalsIgnoreCase(name))
                .findFirst()
                .get();
    }

    public double apply(double num){
        return function.apply(num);
    }
}
