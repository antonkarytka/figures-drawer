package Shapes;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@XStreamAlias("rectangle")
public class Rectangle extends Shape implements Editable, Selectable {

    public Rectangle() {}

    public Rectangle (Point topLeft, int width, int height) {
        addPoint(topLeft);
        addPoint(new Point(topLeft.x + width, topLeft.y));
        addPoint(new Point(topLeft.x + width, topLeft.y + height));
        addPoint(new Point(topLeft.x, topLeft.y + height));
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
