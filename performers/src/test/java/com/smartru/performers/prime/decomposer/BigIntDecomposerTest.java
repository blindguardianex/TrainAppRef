package com.smartru.performers.prime.decomposer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig
class BigIntDecomposerTest {

    @Test
    void decompose() {
        BigIntDecomposer decomposer = new BigIntDecomposer();
        BigInteger num = BigInteger.valueOf(12738213);
        String result = decomposer.decompose(num);
        System.out.println(result);
    }
}