package com.smartru.prime.checker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Arrays;

@Slf4j
@Component
public class LongPrimeNumberChecker {

    private Long[]miniPrimes = new Long[]{2L,3L,5L,7L,11L,13L,17L,19L,23L,29L,32L,37L,41L,43L,47L,49L,53L,59L};

    public boolean isPrimeNumber(long num){
        if (checkForMiniPrime(num)){
            return true;
        }
        if(fastCheck(num)){
            return false;
        }
        if (num<Integer.MAX_VALUE){
            return longCheck(num);
        }
        BigInteger bigIntNum = BigInteger.valueOf(num);
        if(bigIntNum.isProbablePrime(1000000)){
            return longCheck(num);
        }
        return false;
    }

    /**
     * Проверяет, делится ли число на 2 или 5
     * @param num
     * @return
     */
    private boolean fastCheck(long num){
        return num%2==0 ||
                num%3==0 ||
                num%5==0 ||
                num%7==0 ||
                num%11==0 ||
                num%13==0 ||
                num%17==0 ||
                num%19==0 ||
                num%23==0 ||
                num%29==0;
    }

    /**
     * Проверяются следующие делители: 3, 7, 11, 13, 17, 19, 23, 29. Таким образом
     * (в совокупности с предыдещими проверками) мы отсеяли все числа от 2 до 30 в
     * качестве возможных делителей проверяемого числа.
     * Далее устанавливает границу проверки: sqrt из нашего числа (дальнейший перебор бессмысленнен,
     * т.к. наименьший делитель должен быть меньше корня из числа).
     * В дальнейшем мы находим все простые числа от 30 до 60 (их 8: 31, 37, 41, 43, 47, 49, 53, 59)
     * и в работаем только с ними: после каждой проверки (итерации) прибавляем 30 к
     * вышеназванным числам (поскольку все остальные числа так или иначе имеют общие делители
     * с уже проверенными числами). Проверка происходит до тех пор, пока минимальное
     * (31) число не выйдет за рамки проверки. Если это произойдет, значит, число является простым
     * @param num
     * @return
     */
    private boolean longCheck(final long num){
        int bound = (int) Math.sqrt(num);
        if (bound<32){
            return enumerationCheck(num);
        }

        long thirtyOne = 32L;
        long thirtySeven = 37L;
        long fortyOne = 41L;
        long fortyThree = 43L;
        long fortySeven = 47L;
        long fortyNine = 49L;
        long fiftyThree = 53L;
        long fiftyNine = 59L;

        while (checkBound(thirtyOne,bound) || (
                num%thirtyOne==0 ||
                        num%thirtySeven==0 ||
                        num%fortyOne==0 ||
                        num%fortyThree==0 ||
                        num%fortySeven==0 ||
                        num%fortyNine==0 ||
                        num%fiftyThree==0 ||
                        num%fiftyNine==0)){
            thirtyOne += 30L;
            thirtySeven += 30L;
            fortyOne += 30L;
            fortyThree += 30L;
            fortySeven += 30L;
            fortyNine += 30L;
            fiftyThree += 30L;
            fiftyNine += 30L;
        }
        if(thirtyOne>bound){
            return false;
        }
        return true;
    }

    private boolean checkForMiniPrime(final long num){
        return Arrays.stream(miniPrimes)
                .mapToLong(l->l)
                .filter(l->num==l)
                .findFirst()
                .isPresent();
    }

    private boolean checkBound(long min, long bound){
        return min<bound;
    }

    private boolean enumerationCheck(long num){
        for (long i=2;i<Math.sqrt(num);i++){
            if (num%i==0){
                return false;
            }
        }
        return true;
    }
}
