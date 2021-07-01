package com.smartru.performer.model;

import com.smartru.prime.checker.DelegatePrimeNumberChecker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringJUnitConfig
class PrimeNumberCheckerTest {

    @Autowired
    private DelegatePrimeNumberChecker checker;
    private static final String CHECKED_NUM = "2305843009213693951";

    @Test
    void isPrimeNumber() {
        log.info("Starting test prime number checker...");
        long start = System.nanoTime();
        boolean isPrime = checker.isPrimeNumber(CHECKED_NUM);
        long end = System.nanoTime();
        long seconds = TimeUnit.NANOSECONDS.toSeconds(end-start);
        log.info("Finish check by: {} seconds. Result: {}.",seconds, isPrime);
    }
}