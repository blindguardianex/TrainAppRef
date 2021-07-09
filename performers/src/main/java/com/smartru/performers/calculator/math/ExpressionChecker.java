package com.smartru.performers.calculator.math;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionChecker {

    private final String OPERATORS_PATTERN = "[+\\-*/^]";
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("[ +\\-*/^[[+-]?([0-9]+([.,][0-9]*)?|[.,][0-9]+)]()]*");
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("(SQRT|SIN|COS|TAN|CTN|ASIN|ACOS|ATAN|ACTN|ABS|LG|LN|DEGREES|RADIANS)*");

    public static boolean isExpression(String expression){
        expression = expression.toUpperCase();

        Matcher functionMatcher = FUNCTION_PATTERN.matcher(expression);
        expression = functionMatcher.replaceAll("");

        Matcher expressionMatcher = EXPRESSION_PATTERN.matcher(expression);
        expression = expressionMatcher.replaceAll("");

        return expression.isBlank();
    }
}
