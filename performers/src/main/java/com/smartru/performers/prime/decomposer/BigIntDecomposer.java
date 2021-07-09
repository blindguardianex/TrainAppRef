package com.smartru.performers.prime.decomposer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Component
public class BigIntDecomposer {

    public String decompose(BigInteger num){
        BigInteger bound = num.sqrt();
        BigInteger i = BigInteger.valueOf(2);
        while (i.compareTo(bound)<1){
            if(num.mod(i).equals(BigInteger.ZERO)){
                return i.toString();
            }
            i = i.add(BigInteger.ONE);
        }
        return null;
    }
}
