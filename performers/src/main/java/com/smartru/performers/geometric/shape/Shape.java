package com.smartru.performers.geometric.shape;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.function.Supplier;

public abstract class Shape {

    protected ObjectNode properties;

    public abstract Desired getDesired(String desiredProperty);

    public void setProperties(ObjectNode properties){
        this.properties=properties;
    }

    public enum Type{
        CIRCLE("Круг", Circle::new),
        RECTANGLE("Прямоугольник", Rectangle::new);

        private final String rusName;
        private final Supplier<Shape> supplier;

        Type(String rusName, Supplier<Shape> supplier) {
            this.rusName = rusName;
            this.supplier = supplier;
        }

        public String getRusName() {
            return rusName;
        }

        public static Shape get(String name){
            return Arrays.stream(Type.values())
                    .filter(type -> type.name().equals(name))
                    .map(Type::get)
                    .findFirst()
                    .get();
        }

        private Shape get(){
            return supplier.get();
        }
    }
}
