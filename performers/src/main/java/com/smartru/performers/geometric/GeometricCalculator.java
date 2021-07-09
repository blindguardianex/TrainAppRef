package com.smartru.performers.geometric;

import com.smartru.common.model.Calculator;
import com.smartru.performers.geometric.shape.Desired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Slf4j
@Component
@Qualifier("geometricCalculator")
public class GeometricCalculator implements Calculator {

    @Override
    public double solve(String stringProblem) throws ParseException {
        GeometricProblem problem = GeometricProblem.fromString(stringProblem);
        return solve(problem);
    }

    /**
     * TODO: Добавить проверку корректности условий
     * @param problem
     * @return
     */
    public double solve(GeometricProblem problem){
        Desired desiredProperty = problem.getShape().getDesired(problem.getUnknownProperty());
        double result = desiredProperty.calculate(problem.getShape());
        return result;
    }
}
