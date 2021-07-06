package com.smartru.performers.calculator.math;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig
class BracketCheckerTest {

    @Test
    void check() {
        log.info("Start bracket checker test...");
        System.out.println();
        log.info("Start successful tests...");
        assertFalse(BracketsChecker.check("((dasf)(("));
        assertFalse(BracketsChecker.check("())(sdfs)"));
        assertFalse(BracketsChecker.check("(dsf((sdf())"));
        log.info("Successfully!");
        System.out.println();

        log.info("Start successfully tests...");
        assertTrue(BracketsChecker.check("()dsf()()sdf"));
        assertTrue(BracketsChecker.check("(((sdf)(dsf)))"));
        assertTrue(BracketsChecker.check("()(sdf()(())sd)"));
        log.info("Successfully!");
    }
}