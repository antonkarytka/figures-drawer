package Shapes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("line")
public class Line extends Polyline {

    public Line() {}

    public Line(Point startPoint, Point endPoint) {
        addPoint(startPoint);
        addPoint(endPoint);
    }

    @Override
    public void refreshFigure(Point point) {
        if (points.size() > 1) {
            points.remove(points.size() - 1);
            addPoint(point);
        } else {
            addPoint(point);
        }
    }
}
