package com.smartru.performers.geometric.shape;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor
public class Circle extends Shape{

    public Circle(ObjectNode properties) {
        this.properties = properties;
    }

    @Override
    public Desired getDesired(String desiredProperty) {
        return Property.valueOf(desiredProperty);
    }

    public double radius(){
        System.out.println("Ищем радиус...");
        if (isAvailable(Property.RADIUS)){
            return get(Property.RADIUS);
        } else if(canFind(Property.DIAMETER)){
            return radiusByDiameter();
        } else if (canFind(Property.SQUARE)){
            return radiusBySquare();
        } else if (canFind(Property.PERIMETER)){
            return radiusByPerimeter();
        }

        return -1;
    }

    public double square() {
        System.out.println("Ищем площадь...");
        if (isAvailable(Property.SQUARE)) {
            return get(Property.SQUARE);
        } else if(canFind(Property.RADIUS)){
            return squareByRadius();
        }

        return -1;
    }

    public double perimeter() {
        System.out.println("Ищем периметр...");
        if (isAvailable(Property.PERIMETER)){
            return get(Property.PERIMETER);
        } else if(canFind(Property.RADIUS)){
            return perimeterByRadius();
        }

        return -1;
    }

    public double diameter(){
        System.out.println("Ищем диаметр...");
        if (isAvailable(Property.DIAMETER)){
            return get(Property.DIAMETER);
        } else if(canFind(Property.RADIUS)){
            return diameterByRadius();
        }

        return -1;
    }


    private double squareByRadius(){
        System.out.println("Находим площадь по радиусу...");
        double radius = get(Property.RADIUS);
        double square = Math.PI*radius*radius;
        properties.put(Property.SQUARE.name(), square);
        return square;
    }

    private double perimeterByRadius(){
        System.out.println("Находим периметр по радиусу...");
        double radius = get(Property.RADIUS);
        double perimeter = 2*Math.PI*radius;
        properties.put(Property.PERIMETER.name(), perimeter);
        return perimeter;
    }

    private double radiusByDiameter(){
        System.out.println("Находим радиус по диаметру...");
        double diameter = get(Property.DIAMETER);
        double radius = diameter/2;
        properties.put(Property.RADIUS.name(), radius);
        return radius;
    }

    private double radiusBySquare(){
        System.out.println("Находим радиус по площади...");
        double square = get(Property.SQUARE);
        double radius = Math.sqrt(square/Math.PI);
        properties.put(Property.RADIUS.name(), radius);
        return radius;
    }

    private double radiusByPerimeter(){
        System.out.println("Находим радиус по периметру...");
        double perimeter = get(Property.PERIMETER);
        double radius = perimeter/2/Math.PI;
        properties.put(Property.RADIUS.name(), radius);
        return radius;
    }

    private double diameterByRadius(){
        System.out.println("Находим диаметр по радиусу...");
        double radius = get(Property.RADIUS);
        double diameter = radius*2;
        properties.put(Property.DIAMETER.name(), diameter);
        return diameter;
    }

    private boolean isAvailable(Property property){
        return properties.get(property.name())!=null;
    }

    private boolean canFind(Property... props){
        boolean propsAvailable = true;
        for (Property property:props){
            propsAvailable = propsAvailable && property.calculate(this)!=-1;
        }
        return propsAvailable;
    }

    private double get(Property property){
        return properties.get(property.name()).asDouble();
    }

    public enum Property implements Desired{
        SQUARE("площадь", Circle::square),
        PERIMETER("длинна окружности", Circle::perimeter),
        RADIUS("радиус", Circle::radius),
        DIAMETER("диаметр", Circle::diameter);

        private final String rusName;
        private final Function<Circle, Double> function;

        Property(String rusName, Function<Circle, Double> function) {
            this.rusName = rusName;
            this.function = function;
        }

        @Override
        public double calculate(Shape shape) {
            Circle circle = (Circle) shape;
            return function.apply(circle);
        }

        @Override
        public String getRusName() {
            return rusName;
        }
    }
}
