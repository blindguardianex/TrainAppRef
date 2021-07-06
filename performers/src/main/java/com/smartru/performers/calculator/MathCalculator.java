package com.smartru.performers.calculator;

import com.smartru.common.model.Calculator;
import com.smartru.performers.calculator.math.BracketsChecker;
import com.smartru.performers.calculator.math.BiOperator;
import com.smartru.performers.calculator.math.ExpressionIterator;
import com.smartru.performers.calculator.math.UOperator;
import lombok.extern.slf4j.Slf4j;
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
public class MathCalculator implements Calculator {

    private final String OPERAND_PATTERN = "^[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$";
    private final Comparator<BiOperator>comparator = BiOperator.getComparator();
    private final StringBuilder postfixExpression = new StringBuilder();
    private final StringBuilder operand = new StringBuilder();
    private final Stack<BiOperator> biOperators = new Stack<>();

    private Iterator<Character> expressionIterator;

    @Override
    public double perform(String expression) throws ParseException {
        expression = trimExpression(expression);
        checkBrackets(expression);
        String postfixExpression = infixToPostfix(expression);
        System.out.println("Postfix: "+postfixExpression);
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
            if (isBiOperator(ch)) {
                addActualOperandToExpression();
                parseBiOperator(BiOperator.getOperator(ch));
            } else {
                operand.append(ch);
            }
            if (operandIsUOperator()) {
                processUOperator();
            }
        }
        addActualOperandToExpression();
        addRemainingOperatorsFromStack();
        String result = postfixExpression.toString();
        return result.trim();
    }

    private void parseBiOperator(BiOperator currentOperator){
        if (operatorStackIsEmpty()){
            biOperators.push(currentOperator);
        }
        else if(operatorIsOpenBracket(currentOperator)){
            biOperators.push(currentOperator);
        }
        else if(operatorIsCloseBracket(currentOperator)){
            addSubExpressionToExpression();
        }
        else if (nextOperatorLowerPriority(currentOperator)){
            biOperators.push(currentOperator);
        }
        else {
            BiOperator topOperator = biOperators.pop();
            addToExpression(topOperator);
            biOperators.push(currentOperator);
        }
    }

    private void processUOperator() throws ParseException {
        UOperator currentOperator = getCurrentUOperator();
        flushOperand();

        StringBuilder subExpression = new StringBuilder();
        subExpression.append(nextCharFromExpression());
        while (subExpressionIsNotEnd(subExpression)){
            subExpression.append(nextCharFromExpression());
        }

        MathCalculator subCalculator = new MathCalculator();
        double subExpressionResult = subCalculator.perform(subExpression.toString());
        double result = currentOperator.apply(subExpressionResult);
        operand.append(result);
        addActualOperandToExpression();
    }

    private double calculate(String postfixExpression){
        Stack<Double>operands = new Stack<>();
        String[]expressionElements = postfixExpression.split(" ");
        for(String element:expressionElements){
            if (isBiOperator(element)){
                BiOperator operator = BiOperator.getOperator(element);
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
        expression = expression.replace(" ", "");
        expression = expression.replace(",",".");
        return expression;
    }

    private void addToExpression(BiOperator op){
        if (op!= BiOperator.OPEN_BRACKET && op!= BiOperator.CLOSE_BRACKET) {
            postfixExpression.append(" ");
            postfixExpression.append(op.getOp());
        }
    }

    private char nextCharFromExpression(){
        return expressionIterator.next();
    }

    private UOperator getCurrentUOperator(){
        return UOperator.getOperator(operand.toString());
    }

    private void addActualOperandToExpression() throws ParseException {
        if (operand.length()>0) {
            if (!operand.toString().matches(OPERAND_PATTERN)){
                throw new ParseException("Ошибка в введенном выражении: "+operand,0);
            }
            postfixExpression.append(" ");
            postfixExpression.append(operand);
            flushOperand();
        }
    }

    private void addRemainingOperatorsFromStack(){
        while (!operatorStackIsEmpty()){
            addToExpression(biOperators.pop());
        }
    }

    private void flushOperand(){
        operand.delete(0, operand.length());
    }

    private void addSubExpressionToExpression(){
        while (operatorStackNotEmptyAndNextOperatorNotOpenBracket()){
            addToExpression(biOperators.pop());
        }
    }

    private boolean isBiOperator(char ch){
        return BiOperator.isBiOperator(ch);
    }

    private boolean isBiOperator(String element){
        return BiOperator.isBiOperator(element);
    }

    private boolean operandIsUOperator(){
        return UOperator.isUOperator(operand.toString());
    }

    private boolean operatorStackIsEmpty(){
        return biOperators.isEmpty();
    }

    private boolean operatorIsOpenBracket(BiOperator op){
        return op==BiOperator.OPEN_BRACKET;
    }

    private boolean operatorIsCloseBracket(BiOperator op){
        return op== BiOperator.CLOSE_BRACKET;
    }

    private boolean operatorStackNotEmptyAndNextOperatorNotOpenBracket(){
        return !biOperators.isEmpty() && biOperators.peek()!= BiOperator.OPEN_BRACKET;
    }

    private boolean nextOperatorLowerPriority(BiOperator op){
        return comparator.compare(op, biOperators.peek()) > 0;
    }

    private boolean subExpressionIsNotEnd(CharSequence subExpression){
        return !BracketsChecker.check(subExpression);
    }
}
