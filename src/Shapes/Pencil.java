package Shapes;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;


public class Pencil extends Shape implements Editable {

    public Pencil() {}

    @Override
    public void setColor(Color color) {
        borderColor = color;
    }

    @Override
    public void setLineWidth(int width) {
        lineWidth = width;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(borderColor);
        gc.setLineWidth(lineWidth);
        gc.strokePolyline(getXCoordinates(), getYCoordinates(), getAmountPoints());
    }

    @Override
    public void drawBorder(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokePolyline(getXCoordinates(), getYCoordinates(), getAmountPoints());
    }

    @Override
    public void refreshFigure(Point point) {
        addPoint(point);
    }
}
