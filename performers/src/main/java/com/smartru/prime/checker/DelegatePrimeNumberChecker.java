package com.smartru.prime.checker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DelegatePrimeNumberChecker {

    private static final int MAX_DIGITS_IN_LONG = 19;

    @Autowired
    private BigIntPrimeNumberChecker bigIntChecker;
    @Autowired
    private LongPrimeNumberChecker longChecker;

    public boolean isPrimeNumber(String num){
        if (numIsTooLarge(num)){
            return bigIntChecker.isPrimeNumber(num);
        } else {
            long longNum = Long.parseLong(num);
            return longChecker.isPrimeNumber(longNum);
        }
    }

    private boolean numIsTooLarge(String num){
        return num.length()>=MAX_DIGITS_IN_LONG;
    }
}
