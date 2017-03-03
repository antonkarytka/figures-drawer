package Shapes;

import javafx.scene.canvas.GraphicsContext;

public class Polygon extends Shape {

    public Polygon() {}

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(borderColor);
        gc.setLineWidth(5);
        gc.strokePolygon(getXCoordinates(), getYCoordinates(), getAmountPoints());
    }

    @Override
    public void refreshFigure(Point point) {
        addPoint(point);
    }
}
