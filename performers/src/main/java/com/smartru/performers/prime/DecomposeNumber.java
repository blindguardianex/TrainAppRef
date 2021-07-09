package com.smartru.performers.prime;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.model.BooleanPerformer;
import com.smartru.common.model.Performer;
import com.smartru.common.model.StringPerformer;
import com.smartru.performers.prime.decomposer.DelegateDecomposer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("decomposeNumberPerformer")
public class DecomposeNumber implements StringPerformer {

    private final BooleanPerformer primeChecker;
    private final DelegateDecomposer decomposer;

    @Autowired
    public DecomposeNumber(@Qualifier("PrimeNumberCheckerPerformer") BooleanPerformer primeChecker,
                           DelegateDecomposer decomposer) {
        this.primeChecker = primeChecker;
        this.decomposer = decomposer;
    }

    @Override
    public String perform(Task task) {
        if (numberIsPrime(task)){
            return String.format("%s, %s простое",usernameFromTask(task),task.getNum());
        }
        String result = decomposer.decompose(task.getNum());
        return String.format("%s, %s",usernameFromTask(task), result);
    }

    private boolean numberIsPrime(Task task){
        return primeChecker.perform(task);
    }

    private String usernameFromTask(Task task){
        return task.getProperties().get("username").asText();
    }
}
