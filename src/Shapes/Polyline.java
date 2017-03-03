package Shapes;

import javafx.scene.canvas.*;

public class Polyline extends Shape {

    public Polyline() {}

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(borderColor);
        gc.setLineWidth(5);
        gc.strokePolyline(getXCoordinates(), getYCoordinates(), getAmountPoints());
    }

    @Override
    public void refreshFigure(Point point) {
        addPoint(point);
    }
}
