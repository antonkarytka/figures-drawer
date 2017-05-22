package shapes;

import asbtract.Shape;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import interfaces.Editable;
import interfaces.Selectable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@XStreamAlias("triangle")
public class Triangle extends Shape implements Editable, Selectable {

    public Triangle() {}

    public Triangle(Point a, Point b, Point c) {
        addPoint(a);
        addPoint(b);
        addPoint(c);
    }

    @Override
    public void setColor(Color color) {
        borderColor = color;
    }

    @Override
    public void setLineWidth(int width) {
        lineWidth = width;
    }

    @Override
    public boolean contains(Point point) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).y > point.y) != (points.get(j).y > point.y) &&
                    (point.x < (points.get(j).x - points.get(i).x) * (point.y - points.get(i).y) / (points.get(j).y-points.get(i).y) + points.get(i).x)) {
                result = !result;
            }
        }
        return result;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(borderColor);
        gc.setLineWidth(lineWidth);
        gc.strokePolygon(getXCoordinates(), getYCoordinates(), getAmountPoints());
    }

    @Override
    public void drawBorder(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokePolygon(getXCoordinates(), getYCoordinates(), getAmountPoints());
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
