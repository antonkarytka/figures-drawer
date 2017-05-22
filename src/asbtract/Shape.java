package asbtract;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import shapes.Point;

import java.util.ArrayList;

public abstract class Shape {

    protected ArrayList<Point> points = new ArrayList<>();
    public Color borderColor;
    public int lineWidth;

    public Shape() {}

    public void addPoint(Point point) {
        points.add(point);
    }

    protected double[] getXCoordinates() {
        double[] xCoordinates = new double[points.size()];
        for (int i = 0; i <= points.size() - 1; i++)
            xCoordinates[i] = points.get(i).x;
        return xCoordinates;
    }

    protected double[] getYCoordinates() {
        double[] yCoordinates = new double[points.size()];
        for (int i = 0; i <= points.size() - 1; i++)
            yCoordinates[i] = points.get(i).y;
        return yCoordinates;
    }

    protected int getAmountPoints() {
        return points.size();
    }

    public abstract void draw(GraphicsContext gc);

    public abstract void drawBorder(GraphicsContext gc);

    public abstract void refreshFigure(Point point);


}
