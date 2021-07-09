package com.smartru.performers.calculator;

import com.smartru.common.model.Calculator;
import com.smartru.performers.calculator.math.BracketsChecker;
import com.smartru.performers.calculator.math.MathOperator;
import com.smartru.performers.calculator.math.ExpressionIterator;
import com.smartru.performers.calculator.math.MathFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

/**
 * !!!ВНИМАНИЕ!!!
 * !!!КЛАСС НЕПОТОКОБЕЗОПАСЕН!!!
 */
@Slf4j
@Component
@Scope("prototype")
@Qualifier("mathCalculator")
public class MathCalculator implements Calculator {

    private final String OPERAND_PATTERN = "^[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$";
    private final Comparator<MathOperator>comparator = MathOperator.getComparator();
    private final StringBuilder postfixExpression = new StringBuilder();
    private final StringBuilder operand = new StringBuilder();
    private final Stack<MathOperator> mathOperators = new Stack<>();

    private Iterator<Character> expressionIterator;

    @Override
    public double solve(String expression) throws ParseException {
        expression = trimExpression(expression);
        checkBrackets(expression);
        String postfixExpression = infixToPostfix(expression);
        double result = calculate(postfixExpression);
        return result;
    }

    private void checkBrackets(String expression) throws ParseException {
        if (!BracketsChecker.check(expression)){
            throw new ParseException("Некорректно расставленные скобки!", 0);
        }
    }

    private String infixToPostfix(String expression) throws ParseException {
        expressionIterator = new ExpressionIterator(expression);
        while (expressionIterator.hasNext()) {
            char ch = nextCharFromExpression();
            if (isMathOperator(ch)) {
                addActualOperandToExpression();
                parseMathOperator(MathOperator.getMathOperatorBy(ch));
            } else {
                operand.append(ch);
            }
            if (operandIsMathFunction()) {
                processMathFunction();
            }
        }
        addActualOperandToExpression();
        addRemainingOperatorsFromStack();
        String result = postfixExpression.toString();
        flushAll();
        return result.trim();
    }

    private void parseMathOperator(MathOperator currentOperator){
        if (operatorStackIsEmpty()){
            mathOperators.push(currentOperator);
        }
        else if(operatorIsOpenBracket(currentOperator)){
            mathOperators.push(currentOperator);
        }
        else if(operatorIsCloseBracket(currentOperator)){
            addSubExpressionToExpression();
        }
        else if (prevOperatorLowerPriority(currentOperator)){
            mathOperators.push(currentOperator);
        }
        else {
            MathOperator topOperator = mathOperators.pop();
            addToExpression(topOperator);
            mathOperators.push(currentOperator);
        }
    }

    private void processMathFunction() throws ParseException {
        MathFunction currentOperator = getCurrentMathFunction();
        flushOperand();

        StringBuilder subExpression = new StringBuilder();
        subExpression.append(nextCharFromExpression());
        while (subExpressionIsNotEnd(subExpression)){
            subExpression.append(nextCharFromExpression());
        }

        MathCalculator subCalculator = new MathCalculator();
        double subExpressionResult = subCalculator.solve(subExpression.toString());
        double result = currentOperator.apply(subExpressionResult);
        operand.append(result);
        addActualOperandToExpression();
    }

    private double calculate(String postfixExpression){
        Stack<Double>operands = new Stack<>();
        String[]expressionElements = postfixExpression.split(" ");
        for(String element:expressionElements){
            if (isMathOperator(element)){
                MathOperator operator = MathOperator.getMathOperatorBy(element);
                Double operand2 = operands.pop();
                Double operand1 = operands.pop();
                operands.push(operator.apply(operand1, operand2));
            }
            else {
                operands.push(Double.parseDouble(element));
            }
        }
        return operands.pop();
    }

    private String trimExpression(String expression){
        if (expression.startsWith("+") ||
                expression.startsWith("-")){
            expression = "0"+expression;
        }
        else if (expression.startsWith("(+") ||
                expression.startsWith("(-")){
            expression = "(0"+expression.substring(1);
        }

        expression = expression.replace(" ", "");
        expression = expression.replace(",",".");
        return expression;
    }

    private void addToExpression(MathOperator op){
        if (op!= MathOperator.OPEN_BRACKET && op!= MathOperator.CLOSE_BRACKET) {
            postfixExpression.append(" ");
            postfixExpression.append(op.getOp());
        }
    }

    private char nextCharFromExpression(){
        return expressionIterator.next();
    }

    private MathFunction getCurrentMathFunction(){
        return MathFunction.getMathFunctionBy(operand.toString());
    }

    private void addActualOperandToExpression() throws ParseException {
        if (operand.length()>0) {
            if (!operand.toString().matches(OPERAND_PATTERN)){
                String errorOperand = operand.toString();
                flushAll();
                throw new ParseException("Ошибка в введенном выражении: "+errorOperand,0);
            }
            postfixExpression.append(" ");
            postfixExpression.append(operand);
            flushOperand();
        }
    }

    private void addRemainingOperatorsFromStack(){
        while (!operatorStackIsEmpty()){
            addToExpression(mathOperators.pop());
        }
    }

    private void flushOperand(){
        operand.delete(0, operand.length());
    }

    private void flushAll(){
        operand.delete(0, operand.length());
        mathOperators.clear();
        postfixExpression.delete(0,postfixExpression.length());
    }

    private void addSubExpressionToExpression(){
        while (operatorStackNotEmptyAndNextOperatorNotOpenBracket()){
            addToExpression(mathOperators.pop());
        }
    }

    private boolean isMathOperator(char ch){
        return MathOperator.isMathOperator(ch);
    }

    private boolean isMathOperator(String element){
        return MathOperator.isMathOperator(element);
    }

    private boolean operandIsMathFunction(){
        return MathFunction.isMathFunction(operand.toString());
    }

    private boolean operatorStackIsEmpty(){
        return mathOperators.isEmpty();
    }

    private boolean operatorIsOpenBracket(MathOperator op){
        return op== MathOperator.OPEN_BRACKET;
    }

    private boolean operatorIsCloseBracket(MathOperator op){
        return op== MathOperator.CLOSE_BRACKET;
    }

    private boolean operatorStackNotEmptyAndNextOperatorNotOpenBracket(){
        return !mathOperators.isEmpty() && mathOperators.peek()!= MathOperator.OPEN_BRACKET;
    }

    private boolean prevOperatorLowerPriority(MathOperator op){
        return comparator.compare(op, mathOperators.peek()) > 0;
    }

    private boolean subExpressionIsNotEnd(CharSequence subExpression){
        return !BracketsChecker.check(subExpression);
    }
}
