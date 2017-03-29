package Shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public abstract class Shape {
    public Color borderColor;
    public ArrayList<Point> points = new ArrayList<>();

    Shape() {}

    public void addPoint(Point point) {
        points.add(point);
    }

    double[] getXCoordinates() {
        double[] xCoordinates = new double[points.size()];
        for (int i = 0; i <= points.size() - 1; i++)
            xCoordinates[i] = points.get(i).x;
        return xCoordinates;
    }

    double[] getYCoordinates() {
        double[] yCoordinates = new double[points.size()];
        for (int i = 0; i <= points.size() - 1; i++)
            yCoordinates[i] = points.get(i).y;
        return yCoordinates;
    }

    int getAmountPoints() {
        return points.size();
    }

    public abstract boolean contains(Point point);

    public abstract void draw(GraphicsContext gc);

    public abstract void refreshFigure(Point point);
}
