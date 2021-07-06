package com.smartru.performers.calculator.math;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionCheckerTest {

    @Test
    void isExpression() {
        String expression = "-13+SQRT(6+SQRT(9))/sin(30)";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "3*/(4+5)-6/(1+2)";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "(4.06*0.0058+3,3044895-(0.7584/2.37+0,0003/8))/0,03625*80-2.43";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "(SIN((3+4)*5))^5+COS((4+SQRT(3*2)))^3*2";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "5^2-1";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "5^2-1=5";
        System.out.println(expression);
        assertFalse(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "asd";
        System.out.println(expression);
        assertFalse(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "asd+2-120";
        System.out.println(expression);
        assertFalse(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "asd 2-24+213 ва";
        System.out.println(expression);
        assertFalse(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "LG(100)";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "LN(8)";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "ABS(1)";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "ABS(-1)";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "DEGREES(2)";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();

        expression = "RADIANS(60)";
        System.out.println(expression);
        assertTrue(ExpressionChecker.isExpression(expression));
        System.out.println();
    }
}