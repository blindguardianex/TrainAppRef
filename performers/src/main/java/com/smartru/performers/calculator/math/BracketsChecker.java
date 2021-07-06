package com.smartru.performers.calculator.math;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Slf4j
public class BracketsChecker {


    public static boolean check(CharSequence expression){
        Stack<Character>expressionBrackets = new Stack<>();
        for (int i=0;i<expression.length();i++){
            char bracket = expression.charAt(i);
            switch (bracket){
                case '(':
                    expressionBrackets.push(bracket);
                    break;
                case ')':
                    if (!expressionBrackets.isEmpty()){
                        if (expressionBrackets.pop()!='('){
                            return false;
                        }
                    } else {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        if (expressionBrackets.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
