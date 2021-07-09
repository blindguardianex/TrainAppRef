package com.smartru.performers.factorial;

import com.smartru.common.entity.Task;
import com.smartru.common.model.StringPerformer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.stream.IntStream;

@Component
@Qualifier("factorialPerformer")
public class FactorialPerformer implements StringPerformer {

    @Override
    public String perform(Task task) {
        int bound = Integer.parseInt(task.getNum())+1;
        BigInteger result = IntStream.range(1,bound)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger::multiply)
                .get();
        return result.toString();
    }
}
