package Shapes;

public class Square extends Polygon {

    public Square() {}

    public Square(Point topLeft, int sideLength) {
        addPoint(topLeft);
        addPoint(new Point(topLeft.x + sideLength, topLeft.y));
        addPoint(new Point(topLeft.x + sideLength, topLeft.y + sideLength));
        addPoint(new Point(topLeft.x, topLeft.y + sideLength));
    }

    @Override
    public void refreshFigure(Point point) {
        int sideLength = point.x - points.get(0).x;
        if (points.size() == 1) {
            addPoint(new Point(points.get(0).x + sideLength, points.get(0).y));
            addPoint(new Point(points.get(0).x + sideLength, points.get(0).y + sideLength));
            addPoint(new Point(points.get(0).x, points.get(0).y + sideLength));
        } else {
            points.remove(3);
            points.remove(2);
            points.remove(1);
            addPoint(new Point(points.get(0).x + sideLength, points.get(0).y));
            addPoint(new Point(points.get(0).x + sideLength, points.get(0).y + sideLength));
            addPoint(new Point(points.get(0).x, points.get(0).y + sideLength));
        }
    }

}
