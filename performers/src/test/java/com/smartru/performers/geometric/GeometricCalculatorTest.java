package com.smartru.performers.geometric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smartru.common.model.Calculator;
import com.smartru.performers.geometric.shape.Circle;
import com.smartru.performers.geometric.shape.Rectangle;
import com.smartru.performers.geometric.shape.Shape;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.text.ParseException;

@Slf4j
@SpringJUnitConfig
class GeometricCalculatorTest {

    private static ObjectMapper mapper = new ObjectMapper();

    private static String PROBLEM_ONE;
    static {
        ObjectNode props = mapper.createObjectNode();
        props.put(Circle.Property.RADIUS.name(), 5);

        ObjectNode problem = mapper.createObjectNode();
        problem.put(GeometricProblem.Parameters.SHAPE.name(), Shape.Type.CIRCLE.name());
        problem.put(GeometricProblem.Parameters.PROPS.name(), props.toString());
        problem.put(GeometricProblem.Parameters.UNKNOWN_PROPERTY.name(), Circle.Property.SQUARE.name());
        PROBLEM_ONE = problem.toString();
    }
    private static String PROBLEM_TWO;
    static {
        ObjectNode props = mapper.createObjectNode();
        props.put(Rectangle.Property.HEIGHT.name(), 3)
                .put(Rectangle.Property.SQUARE.name(), 12);

        ObjectNode problem = mapper.createObjectNode();
        problem.put(GeometricProblem.Parameters.SHAPE.name(), Shape.Type.RECTANGLE.name());
        problem.put(GeometricProblem.Parameters.PROPS.name(), props.toString());
        problem.put(GeometricProblem.Parameters.UNKNOWN_PROPERTY.name(), Rectangle.Property.DIAGONAL.name());
        PROBLEM_TWO = problem.toString();
    }
    private static String PROBLEM_THREE;
    static {
        ObjectNode props = mapper.createObjectNode();
        props.put(Circle.Property.SQUARE.name(), 157);

        ObjectNode problem = mapper.createObjectNode();
        problem.put(GeometricProblem.Parameters.SHAPE.name(), Shape.Type.CIRCLE.name());
        problem.put(GeometricProblem.Parameters.PROPS.name(), props.toString());
        problem.put(GeometricProblem.Parameters.UNKNOWN_PROPERTY.name(), Circle.Property.RADIUS.name());
        PROBLEM_THREE = problem.toString();
    }

    @Test
    void solve() {
        try {
            System.out.println(PROBLEM_ONE);
            System.out.println(PROBLEM_TWO);
            System.out.println(PROBLEM_THREE);
            Calculator calculator = new GeometricCalculator();
            System.out.println(calculator.solve(PROBLEM_ONE));
            System.out.println(calculator.solve(PROBLEM_TWO));
            System.out.println(calculator.solve(PROBLEM_THREE));
        } catch (ParseException e) {
            System.out.println("Что то пошло не так...");
            e.printStackTrace();
        }
    }
}