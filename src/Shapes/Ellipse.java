package Shapes;

import javafx.scene.canvas.GraphicsContext;

public class Ellipse extends Shape {

    Point center;
    public int width;
    public int height;

    public Ellipse() {}

    @Override
    public boolean contains(Point point) {
        return ((point.x*point.x)/(width*width) + (point.y*point.y)/(height*height)) <= 1;
    }

    public Ellipse(Point center, int width, int height) {
        this.center = center;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(borderColor);
        gc.setLineWidth(5);
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
