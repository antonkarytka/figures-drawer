package Shapes;

public class Circle extends Ellipse {

    public Circle() {}

    public Circle(Point center, int radius) {
        this.center = center;
        this.width = radius;
        this.height = radius;
    }

    @Override
    public void refreshFigure(Point point) {
        center = points.get(0);
        width = Math.abs(points.get(0).x - point.x);
        height = width;
    }

    @Override
    public boolean contains(Point point) {
        return ((Math.pow((point.x - points.get(0).x), 2) + Math.pow((point.y - points.get(0).y), 2) < Math.pow(width, 2)));
    }
}
