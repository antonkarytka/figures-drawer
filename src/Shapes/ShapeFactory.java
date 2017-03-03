package Shapes;

import java.util.HashMap;
import java.util.Map;

public class ShapeFactory {

    public ShapeFactory() {}

    private final Map<String, Object> shapes = createMap();
    private Map<String, Object> createMap()
    {
        Map<String, Object> shapes = new HashMap<String, Object>();
        shapes.put("Pencil", new Polyline());
        shapes.put("Line", new Line());
        shapes.put("Triangle", new Triangle());
        shapes.put("Square", new Square());
        shapes.put("Rectangle", new Rectangle());
        shapes.put("Ellipse", new Ellipse());
        shapes.put("Circle", new Circle());
        return shapes;
    }

    public Shape getShape(String shapeType) {
        return (Shape) shapes.get(shapeType);
    }
}
