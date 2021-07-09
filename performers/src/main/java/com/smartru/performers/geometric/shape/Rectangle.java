package com.smartru.performers.geometric.shape;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor
public class Rectangle extends Shape{

    public Rectangle(ObjectNode properties){
        this.properties=properties;
    }

    @Override
    public Desired getDesired(String desiredProperty) {
        return Property.valueOf(desiredProperty);
    }

    public double height() {
        System.out.println("Ищем ширину...");
        if (isAvailable(Property.HEIGHT)) {
            return get(Property.HEIGHT);
        } else if (isAvailable(Property.WIDTH)) {
            if (isAvailable(Property.SQUARE)) {
                return heightByWidthAndSquare();
            } else if (isAvailable(Property.PERIMETER)) {
                return heightByWidthAndPerimeter();
            } else if (isAvailable(Property.DIAGONAL)) {
                return heightByWidthAndDiagonal();
            }
        }

        return -1;
    }

    public double width(){
        System.out.println("Ищем высоту...");
        if (isAvailable(Property.WIDTH)){
            return get(Property.WIDTH);
        } else if (isAvailable(Property.HEIGHT)){
            if (isAvailable(Property.SQUARE)){
                return widthByHeightAndSquare();
            } else if (isAvailable(Property.PERIMETER)){
                return widthByHeightAndPerimeter();
            } else if (isAvailable(Property.DIAGONAL)){
                widthByHeightAndDiagonal();
            }
        }

        return -1;
    }

    public double square() {
        System.out.println("Ищем площадь...");
        if (isAvailable(Property.SQUARE)){
            return get(Property.SQUARE);
        } else if(canFind(Property.HEIGHT,Property.WIDTH)){
            return squareByHeightAndWidth();
        } else {
            return -1;
        }
    }

    public double perimeter() {
        System.out.println("Ищем периметр...");
        if (isAvailable(Property.PERIMETER)){
            return get(Property.PERIMETER);
        } else if (canFind(Property.HEIGHT,Property.WIDTH)){
            return perimeterByHeightAndWidth();
        } else {
            return -1;
        }
    }

    public double diagonal(){
        System.out.println("Ищем диагональ...");
        if (isAvailable(Property.DIAGONAL)){
            return get(Property.DIAGONAL);
        }
        if (canFind(Property.HEIGHT,Property.WIDTH)){
            return diagonalByPythagoreanTheorem();
        }

        return -1;
    }

    public double angleWidth(){
        System.out.println("Ищем угол между диагональю и высотой...");
        if (isAvailable(Property.ANGLE_WIDTH)){
            return get(Property.ANGLE_WIDTH);
        } else if (canFind(Property.ANGLE_HEIGHT)){
            return angleWidthByAngleHeight();
        } else if (canFind(Property.WIDTH, Property.DIAGONAL)){
            return angleWidthByWidthAndDiagonal();
        }

        return -1;
    }

    public double angleHeight(){
        System.out.println("Ищем угол между диагональю и шириной...");
        if (isAvailable(Property.ANGLE_HEIGHT)){
            return get(Property.ANGLE_HEIGHT);
        } else if (isAvailable(Property.ANGLE_WIDTH)){
            return angleHeightByAngleWidth();
        } else if (canFind(Property.HEIGHT, Property.DIAGONAL)){
            return angleHeightByHeightAndDiagonal();
        }

        return -1;
    }

    private double heightByWidthAndSquare(){
        System.out.println("Находим ширину по высоте и площади...");
        double width = get(Property.WIDTH);
        double square = get(Property.SQUARE);
        double height = square/width;
        properties.put(Property.HEIGHT.name(), height);
        return height;
    }

    private double heightByWidthAndPerimeter(){
        System.out.println("Находим ширину по высоте и периметру...");
        double width = get(Property.WIDTH);
        double perimeter = get(Property.PERIMETER);
        double height = (perimeter/2)-width;
        properties.put(Property.HEIGHT.name(), height);
        return height;
    }

    private double heightByWidthAndDiagonal(){
        System.out.println("Находим ширину по высоте и диагонали...");
        double width = get(Property.WIDTH);
        double diagonal = get(Property.DIAGONAL);
        double height = Math.sqrt(diagonal*diagonal-width*width);
        properties.put(Property.HEIGHT.name(), height);
        return height;
    }

    private double widthByHeightAndSquare(){
        System.out.println("Находим высоту по ширине и площади...");
        double height = get(Property.HEIGHT);
        double square = get(Property.SQUARE);
        double width = square/height;
        properties.put(Property.WIDTH.name(), width);
        return width;
    }

    private double widthByHeightAndPerimeter(){
        System.out.println("Находим высоту по ширине и периметру...");
        double height = get(Property.HEIGHT);
        double perimeter = get(Property.PERIMETER);
        double width = (perimeter/2)-height;
        properties.put(Property.WIDTH.name(), width);
        return width;
    }

    private double widthByHeightAndDiagonal(){
        System.out.println("Находим высоту по ширине и диагонали...");
        double height = get(Property.HEIGHT);
        double diagonal = get(Property.DIAGONAL);
        double width = Math.sqrt(diagonal*diagonal-height*height);
        properties.put(Property.WIDTH.name(), width);
        return height;
    }

    private double squareByHeightAndWidth(){
        System.out.println("Находим площадь по высоте и ширине...");
        double height = get(Property.HEIGHT);
        double width = get(Property.WIDTH);
        double square = height*width;
        properties.put(Property.SQUARE.name(), square);
        return square;
    }

    private double perimeterByHeightAndWidth(){
        System.out.println("Находим периметр по высоте и ширине...");
        double height = get(Property.HEIGHT);
        double width = get(Property.WIDTH);
        double perimeter = (height+width)*2;
        properties.put(Property.PERIMETER.name(), perimeter);
        return perimeter;
    }

    private double diagonalByPythagoreanTheorem(){
        System.out.println("Находим диагональ по теореме Пифагора...");
        double height = get(Property.HEIGHT);
        double width = get(Property.WIDTH);
        double diagonal = Math.sqrt(width*width+height*height);
        properties.put(Property.DIAGONAL.name(), diagonal);
        return diagonal;
    }

    private double angleWidthByAngleHeight(){
        System.out.println("Находим угол между высотой и диагональю по смежному углу...");
        double angleHeight = get(Property.ANGLE_HEIGHT);
        double angleWidth = 90-angleHeight;
        properties.put(Property.ANGLE_WIDTH.name(), angleWidth);
        return angleWidth;
    }

    private double angleWidthByWidthAndDiagonal(){
        System.out.println("Находим угол между высотой и диагональю через высоту и диагональ...");
        double width = get(Property.WIDTH);
        double diagonal = get(Property.DIAGONAL);
        double angleWidthInRadian = Math.acos(width/diagonal);
        double angleWidth = Math.toDegrees(angleWidthInRadian);
        properties.put(Property.ANGLE_WIDTH.name(), angleWidth);
        return angleWidth;
    }

    private double angleHeightByAngleWidth(){
        System.out.println("Находим угол между шириной и диагональю по смежному углу...");
        double angleWidth = get(Property.ANGLE_WIDTH);
        double angleHeight = 90-angleWidth;
        properties.put(Property.ANGLE_HEIGHT.name(), angleHeight);
        return angleHeight;
    }

    private double angleHeightByHeightAndDiagonal(){
        System.out.println("Находим угол между шириной и диагональю через ширину и диагональ...");
        double height = get(Property.HEIGHT);
        double diagonal = get(Property.DIAGONAL);
        double angleHeightInRadian = Math.acos(height/diagonal);
        double angleHeight = Math.toDegrees(angleHeightInRadian);
        properties.put(Property.ANGLE_HEIGHT.name(), angleHeight);
        return angleHeight;
    }

    private boolean isAvailable(Property property){
        return properties.get(property.name())!=null;
    }

    private boolean canFind(Property ... props){
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
        SQUARE("площадь", Rectangle::square),
        PERIMETER("периметр", Rectangle::perimeter),
        HEIGHT("ширина", Rectangle::height),
        WIDTH("высота", Rectangle::width),
        DIAGONAL("диагональ", Rectangle::diagonal),
        ANGLE_HEIGHT("угол между диагональю и шириной", Rectangle::angleHeight),
        ANGLE_WIDTH("угол между диагональю и высотой", Rectangle::angleWidth);

        private final String rusName;
        private final Function<Rectangle, Double> function;

        Property(String rusName, Function<Rectangle, Double> function) {
            this.rusName = rusName;
            this.function = function;
        }

        @Override
        public double calculate(Shape shape) {
            Rectangle rectangle = (Rectangle) shape;
            return function.apply(rectangle);
        }

        @Override
        public String getRusName() {
            return rusName;
        }
    }
}
