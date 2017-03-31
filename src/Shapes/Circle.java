package Shapes;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@XStreamAlias("circle")
public class Circle extends Shape implements Editable, Selectable {

    private Point center;
    private int width;

    public Circle() {}

    public Circle(Point center, int radius) {
        this.center = center;
        this.width = radius;
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
        return ((Math.pow((point.x - points.get(0).x), 2) + Math.pow((point.y - points.get(0).y), 2) < Math.pow(width, 2)));
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(borderColor);
        gc.setLineWidth(lineWidth);
        center = points.get(0);
        gc.strokeOval(center.x, center.y, width, width);
    }

    @Override
    public void drawBorder(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        center = points.get(0);
        gc.strokeOval(center.x, center.y, width, width);
    }

    @Override
    public void refreshFigure(Point point) {
        center = points.get(0);
        width = Math.abs(points.get(0).x - point.x);
    }
}
