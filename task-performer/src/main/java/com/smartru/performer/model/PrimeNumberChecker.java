package com.smartru.performer.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

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

    private final BigInteger THREE = new BigInteger("3");
    private final BigInteger SEVEN = new BigInteger("7");
    private final BigInteger ELEVEN = new BigInteger("11");
    private final BigInteger THIRTEEN = new BigInteger("13");
    private final BigInteger SEVENTEEN = new BigInteger("17");
    private final BigInteger NINETEEN = new BigInteger("19");
    private final BigInteger TWENTY_THREE = new BigInteger("23");
    private final BigInteger TWENTY_NINE = new BigInteger("29");

    private final BigInteger THIRTY = new BigInteger("30");

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
        log.info("Maybe it's a prime number...");
        if (findDivisorsBefore30(num)){
            System.out.println("EZ");
            return false;
        }
        BigInteger bound = num.sqrt();
        long startMillis = System.currentTimeMillis();

        //Threadlocal?
        BigInteger thirtyOne = new BigInteger("31");
        BigInteger thirtySeven = new BigInteger("37");
        BigInteger fortyOne = new BigInteger("41");
        BigInteger fortyThree = new BigInteger("43");
        BigInteger fortySeven = new BigInteger("47");
        BigInteger fortyNine = new BigInteger("49");
        BigInteger fiftyThree = new BigInteger("53");
        BigInteger fiftyNine = new BigInteger("59");

        while (thirtyOne.compareTo(bound)<=0 || (
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
        System.out.println("Checking time: "+(System.currentTimeMillis()-startMillis)/1000+" minutes");
        if(thirtyOne.compareTo(bound)<0){
            System.out.println(thirtyOne);
            System.out.println(bound);
            return false;
        }
        return true;
    }

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
