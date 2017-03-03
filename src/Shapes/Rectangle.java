package Shapes;

public class Rectangle extends Polygon{

    public Rectangle() {}

    public Rectangle (Point topLeft, Point bottomRight) {
        addPoint(topLeft);
        addPoint(new Point(bottomRight.x, topLeft.y));
        addPoint(bottomRight);
        addPoint(new Point(topLeft.x, bottomRight.y));
    }

    @Override
    public void refreshFigure (Point point) {
        if (points.size() == 1) {
            addPoint(new Point(point.x, points.get(0).y));
            addPoint(point);
            addPoint(new Point(points.get(0).x, point.y));
        } else {
            points.remove(3);
            points.remove(2);
            points.remove(1);
            addPoint(new Point(point.x, points.get(0).y));
            addPoint(point);
            addPoint(new Point(points.get(0).x, point.y));
        }
    }
}
