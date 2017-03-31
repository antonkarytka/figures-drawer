package Shapes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("rectangle")
public class Rectangle extends Polygon implements Selectable {

    public Rectangle() {}

    public Rectangle (Point topLeft, int width, int height) {
        addPoint(topLeft);
        addPoint(new Point(topLeft.x + width, topLeft.y));
        addPoint(new Point(topLeft.x + width, topLeft.y + height));
        addPoint(new Point(topLeft.x, topLeft.y + height));
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
