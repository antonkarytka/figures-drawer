package Shapes;

public class Triangle extends Polygon {

    public Triangle() {}

    public Triangle(Point a, Point b, Point c) {
        addPoint(a);
        addPoint(b);
        addPoint(c);
    }

    @Override
    public void refreshFigure(Point point) {
        if (points.size() == 1) {
            addPoint(new Point(Math.abs(point.x - points.get(0).x), point.y));
            addPoint(point);
        } else {
            points.remove(2);
            points.remove(1);
            addPoint(new Point(Math.abs(point.x - points.get(0).x), point.y));
            addPoint(point);
        }
    }
}
