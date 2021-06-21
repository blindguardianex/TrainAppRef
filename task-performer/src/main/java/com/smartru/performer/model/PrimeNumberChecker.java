package com.smartru.performer.model;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class PrimeNumberChecker {

    /** Паттерн для проверки переданного числа */
    private final String NUMBER_PATTERN = "\\A\\d*\\Z";

    public boolean isPrimeNumber(String num){
        if (isNumeric(num)){
            if(fastCheck(num)){
                return false;
            }
            BigInteger bigIntNum = new BigInteger(num);
            return bigIntNum.isProbablePrime(1000000);
        }
        return false;
    }

    private boolean isNumeric(String num){
        return num.matches(NUMBER_PATTERN);
    }

    /**
     * Проверяет, делится ли число на 2 или 5
     * @param num
     * @return
     */
    private boolean fastCheck(String num){
        return num.endsWith("0") ||
                num.endsWith("2") ||
                num.endsWith("4") ||
                num.endsWith("5") ||
                num.endsWith("6") ||
                num.endsWith("8");
    }
}
