package com.smartru.performers.geometric.shape;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Slf4j
@SpringJUnitConfig
class CircleTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void square() {
        ObjectNode prop1 = mapper.createObjectNode()
                .put(Circle.Property.RADIUS.name(),5);
        Circle circle1 = new Circle(prop1);
        System.out.println(circle1.square());

        ObjectNode prop2 = mapper.createObjectNode()
                .put(Circle.Property.DIAMETER.name(),18);
        Circle circle2 = new Circle(prop2);
        System.out.println(circle2.square());

        ObjectNode prop3 = mapper.createObjectNode();
        Circle circle3 = new Circle(prop3);
        System.out.println(circle3.square());
    }

    @Test
    void perimeter() {
        ObjectNode prop1 = mapper.createObjectNode()
                .put(Circle.Property.RADIUS.name(),5);
        Circle circle1 = new Circle(prop1);
        System.out.println(circle1.perimeter());

        ObjectNode prop2 = mapper.createObjectNode()
                .put(Circle.Property.DIAMETER.name(),18);
        Circle circle2 = new Circle(prop2);
        System.out.println(circle2.perimeter());

        ObjectNode prop3 = mapper.createObjectNode();
        Circle circle3 = new Circle(prop3);
        System.out.println(circle3.perimeter());
    }
}