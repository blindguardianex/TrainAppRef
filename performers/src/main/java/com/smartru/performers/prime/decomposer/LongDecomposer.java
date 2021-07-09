package com.smartru.performers.prime.decomposer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LongDecomposer {

    public List<Long>decompose(long num, List<Long> acc){
        long bound = (long) Math.sqrt(num);
        long i = 2;
        while (i<=bound){
            if (num%i==0){
                acc.add(i);
                decompose(num/i,acc);
                break;
            }
            i++;
        }
        if (i>bound){
            acc.add(num);
        }
        return acc;
    }
}
