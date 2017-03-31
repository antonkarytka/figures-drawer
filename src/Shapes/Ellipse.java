package Shapes;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@XStreamAlias("ellipse")
public class Ellipse extends Shape implements Editable, Selectable {

    private Point center;
    private int width;
    private int height;

    public Ellipse() {}

    public Ellipse(Point center, int width, int height) {
        this.center = center;
        this.width = width;
        this.height = height;
    }

    @Override
    public void setColor(Color color) {
        borderColor = color;
    }

    @Override
    public void setLineWidth(int width) {
        lineWidth = width;
    }

    public boolean contains(Point point) {
        return ((point.x * point.x) / (width * width) + ((point.y * point.x) / height * height)) <= 1;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(borderColor);
        gc.setLineWidth(lineWidth);
        center = points.get(0);
        gc.strokeOval(center.x, center.y, width, height);
    }

    @Override
    public void drawBorder(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        center = points.get(0);
        gc.strokeOval(center.x, center.y, width, height);
    }

    @Override
    public void refreshFigure(Point point) {
        center = points.get(0);
        width = 2 * Math.abs(points.get(0).x - point.x);
        height = 2 * Math.abs(points.get(0).y - point.y);
    }
}
