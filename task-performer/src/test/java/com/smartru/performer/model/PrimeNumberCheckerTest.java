package com.smartru.performer.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig
class PrimeNumberCheckerTest {

    private static final String CHECKED_NUM = "909090909090909091";

    @Test
    void isPrimeNumber() {
        log.info("Starting test prime number checker...");
        PrimeNumberChecker checker = new PrimeNumberChecker();
        long start = System.nanoTime();
        boolean isPrime = checker.isPrimeNumber(CHECKED_NUM);
        long end = System.nanoTime();
        long seconds = TimeUnit.NANOSECONDS.toSeconds(end-start);
        log.info("Finish check by: {} seconds. Result: {}.",seconds, isPrime);
    }
}