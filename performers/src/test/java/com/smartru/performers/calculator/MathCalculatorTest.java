package com.smartru.performers.calculator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig
class MathCalculatorTest {

    @Test
    void perform() throws ParseException {
        log.info("Start calculate expression tests...");
        MathCalculator calculator = new MathCalculator();

        String expression = "-13+SQRT(6+SQRT(9))/sin(30)";
        double result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,-6.999999999999999);
        System.out.println();

        expression = "3*(4+5)-6/(1+2)";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,25.0);
        System.out.println();

        expression = "(4.06*0.0058+3,3044895-(0.7584/2.37+0,0003/8))/0,03625*80-2.43";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,6635.914827586207);
        System.out.println();

        expression = "(SIN((3+4)*5))^5+COS((4+SQRT(3*2)))^3*2";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,2.0243480932030393);
        System.out.println();

        expression = "5^2-1";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,24.0);
        System.out.println();

        expression = "LG(100)";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,2);
        System.out.println();

        expression = "LN(8)";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,2.0794415416798357);
        System.out.println();

        expression = "ABS(1)";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,1);
        System.out.println();

        expression = "ABS(-1)";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,1);
        System.out.println();

        expression = "DEGREES(2)";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,114.59155902616465);
        System.out.println();

        expression = "RADIANS(60)";
        result = calculator.perform(expression);
        System.out.println("Expression: "+expression);
        System.out.println("Result: "+result);
        assertEquals(result,1.0471975511965976);
        System.out.println();
    }

//    @Test
//    void infixToPostfix() {
//        log.info("Start infix to postfix transformation test...");
//        MathCalculator calculator = new MathCalculator();
//
//        String expression = "A+B-C";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//
//        expression = "A*B+C";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//
//        expression = "A-B/C";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//
//        expression = "A*B/C";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//
//        expression = "A*(B+C)";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//
//        expression = "A*B+C*D";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//
//        expression = "(A+B)*(C-D)";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//
//        expression = "((A+B)*C)-D";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//
//        expression = "A+B*(C-D/(E+F))";
//        System.out.println("Transform expression: "+expression);
//        System.out.println("Transformed expression: "+calculator.infixToPostfix(expression));
//        System.out.println();
//    }
}