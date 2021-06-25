package com.smartru.performer.model.checker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Slf4j
@Component
public class DelegatePrimeNumberChecker {

    private static final int MAX_DIGITS_IN_LONG = 19;

    @Autowired
    private BigIntPrimeNumberChecker bigIntChecker;
    @Autowired
    private LongPrimeNumberChecker longChecker;

    @Deprecated
    public boolean isPrimeNumber(String num){
        if (num.length()<MAX_DIGITS_IN_LONG){
            long longNum = Long.parseLong(num);
            return longChecker.isPrimeNumber(longNum);
        } else {
            return bigIntChecker.isPrimeNumber(num);
        }
    }
}
