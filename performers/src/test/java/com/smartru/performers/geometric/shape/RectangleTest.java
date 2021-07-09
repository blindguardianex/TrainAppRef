package com.smartru.performers.geometric.shape;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Slf4j
@SpringJUnitConfig
class RectangleTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void square() {
        ObjectNode prop1 = mapper.createObjectNode()
                .put(Rectangle.Property.HEIGHT.name(),5)
                .put(Rectangle.Property.WIDTH.name(), 10);
        Rectangle rec1 = new Rectangle(prop1);
        System.out.println(rec1.square());

        ObjectNode prop3 = mapper.createObjectNode()
                .put(Rectangle.Property.HEIGHT.name(), 5);
        Rectangle rec3 = new Rectangle(prop3);
        System.out.println(rec3.square());
    }

    @Test
    void perimeter() {
        ObjectNode prop1 = mapper.createObjectNode()
                .put(Rectangle.Property.HEIGHT.name(),5)
                .put(Rectangle.Property.WIDTH.name(), 10);
        Rectangle rec1 = new Rectangle(prop1);
        System.out.println(rec1.perimeter());

        ObjectNode prop3 = mapper.createObjectNode()
                .put(Rectangle.Property.HEIGHT.name(), 5);
        Rectangle rec3 = new Rectangle(prop3);
        System.out.println(rec3.perimeter());
    }
}