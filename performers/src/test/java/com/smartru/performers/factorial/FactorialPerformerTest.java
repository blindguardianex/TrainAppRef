package com.smartru.performers.factorial;

import com.smartru.common.entity.Task;
import com.smartru.common.model.StringPerformer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig
class FactorialPerformerTest {

    @Test
    void perform() {
        StringPerformer factorial = new FactorialPerformer();
        System.out.println(factorial.perform(new Task("20")));
        assertEquals(factorial.perform(new Task("20")),"2432902008176640000");
    }
}