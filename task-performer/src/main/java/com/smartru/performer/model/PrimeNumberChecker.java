package com.smartru.performer.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Данный класс проверяет числа на простоту
 * Состоит из нескольких последовательных проверок:
 *  1) Возвращает false, если переданная строка не является числом
 * @see PrimeNumberChecker#isNumeric(String)
 *  2) Возвращает false, если число делится на 2 или 5
 * @see PrimeNumberChecker#fastCheck(String)
 *  3) Возвращает false, если не пройдена проверка тестом Миллера-Рабина
 * @see BigInteger#isProbablePrime(int)
 *  4) В случае, если проверка Миллера-Рабина пройдена успешно, проходит тест,
 *  удостоверяющий простоту числа
 * @see PrimeNumberChecker#longCheck(BigInteger)
 */
@Slf4j
@Component
public class PrimeNumberChecker {

    /** Паттерн для проверки переданного числа */
    private final String NUMBER_PATTERN = "\\A\\d*\\Z";

    private final BigInteger THREE = BigInteger.valueOf(3L);
    private final BigInteger SEVEN = BigInteger.valueOf(7L);
    private final BigInteger ELEVEN = BigInteger.valueOf(11L);
    private final BigInteger THIRTEEN = BigInteger.valueOf(13L);
    private final BigInteger SEVENTEEN = BigInteger.valueOf(17L);
    private final BigInteger NINETEEN = BigInteger.valueOf(19L);
    private final BigInteger TWENTY_THREE = BigInteger.valueOf(23L);
    private final BigInteger TWENTY_NINE = BigInteger.valueOf(29L);

    private static final BigInteger THIRTY = BigInteger.valueOf(30L);

    public boolean isPrimeNumber(String num){
        if (isNumeric(num)){
            if(fastCheck(num)){
                return false;
            }
            BigInteger bigIntNum = new BigInteger(num);
            if(bigIntNum.isProbablePrime(1000000)){
                return longCheck(bigIntNum);
            }
        }
        return false;
    }

    private boolean isNumeric(String num){
        return num.matches(NUMBER_PATTERN);
    }

    /**
     * Проверяет, делится ли число на 2 или 5
     * @param num
     * @return
     */
    private boolean fastCheck(String num){
        return num.endsWith("0") ||
                num.endsWith("2") ||
                num.endsWith("4") ||
                num.endsWith("5") ||
                num.endsWith("6") ||
                num.endsWith("8");
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
    private boolean longCheck(BigInteger num){
        log.info("Maybe num #{} is prime number...", num);
        if (findDivisorsBefore30(num)){
            System.out.println("EZ");
            return false;
        }
        BigInteger bound = num.sqrt();
        long startMillis = System.currentTimeMillis();

        BigInteger thirtyOne = BigInteger.valueOf(31L);
        BigInteger thirtySeven = BigInteger.valueOf(37L);
        BigInteger fortyOne = BigInteger.valueOf(41L);
        BigInteger fortyThree = BigInteger.valueOf(43L);
        BigInteger fortySeven = BigInteger.valueOf(47L);
        BigInteger fortyNine = BigInteger.valueOf(49L);
        BigInteger fiftyThree = BigInteger.valueOf(53L);
        BigInteger fiftyNine = BigInteger.valueOf(59L);


        while (checkBound(thirtyOne,bound) || (
                num.mod(thirtyOne).equals(BigInteger.ZERO) ||
                num.mod(thirtySeven).equals(BigInteger.ZERO) ||
                num.mod(fortyOne).equals(BigInteger.ZERO) ||
                num.mod(fortyThree).equals(BigInteger.ZERO) ||
                num.mod(fortySeven).equals(BigInteger.ZERO) ||
                num.mod(fortyNine).equals(BigInteger.ZERO) ||
                num.mod(fiftyThree).equals(BigInteger.ZERO) ||
                num.mod(fiftyNine).equals(BigInteger.ZERO)
                )){
            thirtyOne = thirtyOne.add(THIRTY);
            thirtySeven = thirtySeven.add(THIRTY);
            fortyOne = fortyOne.add(THIRTY);
            fortyThree = fortyThree.add(THIRTY);
            fortySeven = fortySeven.add(THIRTY);
            fortyNine = fortyNine.add(THIRTY);
            fiftyThree = fiftyThree.add(THIRTY);
            fiftyNine = fiftyNine.add(THIRTY);
        }

        System.out.println("Checking time: "+ TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-startMillis)+" minutes");
        if(thirtyOne.compareTo(bound)<0){
            System.out.println(thirtyOne);
            System.out.println(bound);
            return false;
        }
        return true;
    }

    private boolean checkBound(BigInteger min, BigInteger bound){
        return min.compareTo(bound)<=0;
    }

    /**
     * Проверяет все простые делители до 30
     * @param num
     * @return
     */
    private boolean findDivisorsBefore30(BigInteger num){
        return num.mod(THREE).equals(BigInteger.ZERO) ||
                num.mod(SEVEN).equals(BigInteger.ZERO) ||
                num.mod(ELEVEN).equals(BigInteger.ZERO) ||
                num.mod(THIRTEEN).equals(BigInteger.ZERO) ||
                num.mod(SEVENTEEN).equals(BigInteger.ZERO) ||
                num.mod(NINETEEN).equals(BigInteger.ZERO) ||
                num.mod(TWENTY_THREE).equals(BigInteger.ZERO) ||
                num.mod(TWENTY_NINE).equals(BigInteger.ZERO);
    }
}
