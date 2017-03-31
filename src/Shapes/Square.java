package Shapes;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@XStreamAlias("square")
public class Square extends Shape implements Editable, Selectable {

    public Square() {}

    public Square(Point topLeft, int sideLength) {
        addPoint(topLeft);
        addPoint(new Point(topLeft.x + sideLength, topLeft.y));
        addPoint(new Point(topLeft.x + sideLength, topLeft.y + sideLength));
        addPoint(new Point(topLeft.x, topLeft.y + sideLength));
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
