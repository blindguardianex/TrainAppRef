package com.smartru.performers.prime;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.model.BooleanPerformer;
import com.smartru.common.model.Performer;
import com.smartru.performers.PerformHelper;
import com.smartru.performers.prime.checker.DelegatePrimeNumberChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Qualifier("PrimeNumberCheckerPerformer")
public class PrimeNumberCheckPerformer implements BooleanPerformer {

    private final PerformHelper helper;
    private final DelegatePrimeNumberChecker numberChecker;

    @Value("${number-checker.digit-capacity.minimum:17}")
    private int MIN_BOUND_FOR_CHECK_RESULT_IN_DATABASE;

    @Autowired
    public PrimeNumberCheckPerformer(PerformHelper helper, DelegatePrimeNumberChecker numberChecker) {
        this.helper = helper;
        this.numberChecker = numberChecker;
    }

    @Override
    public boolean perform(Task task) {
        if(numIsBig(task)){
            Optional<Task> optTask = helper.findPerformTaskInDatabaseByNum(task.getNum());
            if (optTask.isPresent()){
                return helper.finishTask(task, optTask.get().getResult().isPrime());
            }
        }
        boolean result = numberChecker.isPrimeNumber(task.getNum());
        return helper.finishTask(task, result);
    }

    private boolean numIsBig(Task task){
        return task.getNum().length() >= MIN_BOUND_FOR_CHECK_RESULT_IN_DATABASE;
    }
}
