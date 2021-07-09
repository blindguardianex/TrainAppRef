package com.smartru.performers.calculator.math;

import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * !!!ВНИМАНИЕ!!!
 * !!!При добавлении новой функции ее необходимо добавить в проверяющее регулярное
 * выражение в классе ExpressionChecker!!!
 * @see ExpressionChecker
 */
public enum MathFunction {

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

    private final String appellation;
    private final UnaryOperator<Double> function;

    MathFunction(String appellation, UnaryOperator<Double> function) {
        this.appellation = appellation;
        this.function = function;
    }

    public static boolean isMathFunction(String funcName){
        return Arrays.stream(MathFunction.values())
                .anyMatch(op->op.appellation.equalsIgnoreCase(funcName));
    }

    public static MathFunction getMathFunctionBy(String name){
        return Arrays.stream(MathFunction.values())
                .filter(op->op.appellation.equalsIgnoreCase(name))
                .findFirst()
                .get();
    }

    public double apply(double num){
        return function.apply(num);
    }
}
