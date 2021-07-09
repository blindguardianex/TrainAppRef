package com.smartru.performers.prime.decomposer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DelegateDecomposer {
    private static final int MAX_DIGITS_IN_LONG = 19;

    @Autowired
    private BigIntDecomposer bigIntDecomposer;
    @Autowired
    private LongDecomposer longDecomposer;

    public String decompose(String num){
        if (numIsTooLarge(num)){
            String minDilimer = bigIntDecomposer.decompose(new BigInteger(num));
            return String.format("Число #%s является составным, однако полное разложение на можители займет слишком большое время. Минимальный делитель: %s",num,minDilimer);
        }
        long longNum = Long.parseLong(num);
        List<Long> dilimers = longDecomposer.decompose(longNum, new ArrayList<>());
        String result = dilimers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("x"));
        return String.format("%s= %s",num,result);
    }

    private boolean numIsTooLarge(String num){
        return num.length()>=MAX_DIGITS_IN_LONG;
    }
}
