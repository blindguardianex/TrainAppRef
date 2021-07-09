package com.smartru.performers.prime.decomposer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig
class LongDecomposerTest {

    @Test
    void decompose() {
        LongDecomposer decomposer = new LongDecomposer();
        long longNum = 12635217;
        List<Long> result = decomposer.decompose(longNum, new ArrayList<>());
        result.forEach(System.out::println);
    }
}