package com.smartru.performers.geometric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smartru.performers.geometric.shape.Shape;
import lombok.Getter;

import java.text.ParseException;

@Getter
public class GeometricProblem {

    private final Shape shape;
    private final String unknownProperty;
    private final static ObjectMapper mapper = new ObjectMapper();

    public GeometricProblem(Shape shape, ObjectNode knownProperties, String unknownProperty) {
        this.shape = shape;
        shape.setProperties(knownProperties);
        this.unknownProperty = unknownProperty;
    }

    public static GeometricProblem fromString(String problem) throws ParseException {
        try {
            JsonNode jsonProblem = mapper.readTree(problem);
            String shapeType = jsonProblem.get(Parameters.SHAPE.name()).asText();
            Shape shape = Shape.Type.get(shapeType);
            ObjectNode properties = propertiesFromJsonProblem(jsonProblem);
            String unknown = jsonProblem.get(Parameters.UNKNOWN_PROPERTY.name()).asText();
            return new GeometricProblem(shape, properties, unknown);
        } catch (JsonProcessingException e) {
            throw new ParseException("Невозможно разложить задачу на JSON", 0);
        }
    }

    private static ObjectNode propertiesFromJsonProblem(JsonNode problem) throws JsonProcessingException {
        String stringProps = problem.get(Parameters.PROPS.name()).asText();
        JsonNode jsonProps = mapper.readTree(stringProps);
        ObjectNode properties = jsonProps.deepCopy();
        return properties;
    }

    public enum Parameters{
        SHAPE,
        PROPS,
        UNKNOWN_PROPERTY
    }
}
